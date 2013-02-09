package cs444.parser.symbols.ast;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.Terminal;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public abstract class AModifiersOptSymbol extends ANonTerminal{
    private boolean hasPublic;
    private boolean hasProtected;
    private boolean hasPrivate;

    private boolean hasStatic;
    private boolean hasAbstract;
    private boolean hasFinal;

    private boolean hasNative;

    public static enum ProtectionLevel{PUBLIC, PROTECTED, PRIVATE, NOT_VALID};
    public static enum ImplementationLevel { ABSTRACT, FINAL, NORMAL };

    protected AModifiersOptSymbol(String name) {
        super(name);
    }

    public ProtectionLevel getProtectionLevel(){
        if(hasPublic) return ProtectionLevel.PUBLIC;
        if(hasProtected) return ProtectionLevel.PROTECTED;
        if(hasPrivate) return ProtectionLevel.PRIVATE;
        return defaultProtectionLevel();
    }

    public boolean isStatic(){
        return hasStatic;
    }

    public ImplementationLevel getImplementationLevel(){
        if(hasAbstract) return ImplementationLevel.ABSTRACT;
        if(hasFinal) return ImplementationLevel.FINAL;
        return defaultImplementationLevel();
    }

    public boolean isNative(){
        return hasNative;
    }

    public void checkVisiblilty(String tokenName) throws IllegalModifierException{
        if(hasPublic){
            throw new IllegalModifierException(tokenName, "public");
        }else if(hasPrivate){
            throw new IllegalModifierException(tokenName, "private");
        }else if(hasProtected){
            throw new IllegalModifierException(tokenName, "private");
        }
    }

    public void giveModifier(Terminal t) throws IllegalModifierException{
        switch(t.token.type){
        case PRIVATE:
            if(hasAbstract) throw new IllegalModifierException("private", "abstract");
            checkVisiblilty("private");
            hasPrivate = true;
            throw new IllegalModifierException("private");
        case PUBLIC:
            checkVisiblilty("public");
            hasPublic = true;
            break;
        case PROTECTED:
            checkVisiblilty("protected");
            hasProtected = true;
            break;
        case STATIC:
            if(hasStatic)throw new IllegalModifierException("static", "static");
            if(hasAbstract)throw new IllegalModifierException("static", "abstract");
            hasStatic = true;
            break;
        case FINAL:
            if(hasFinal)throw new IllegalModifierException("final", "final");
            if(hasAbstract)throw new IllegalModifierException("final", "abstract");
            hasFinal = true;
            break;
        case ABSTRACT:
            if(hasFinal)throw new IllegalModifierException("abstract", "final");
            if(hasAbstract)throw new IllegalModifierException("abstract", "abstract");
            if(hasStatic)throw new IllegalModifierException("abstract", "static");
            hasAbstract = true;
            break;
        case NATIVE:
            if(hasNative) throw new IllegalModifierException("native", "native");
        default:
            throw new IllegalModifierException(t.token.type.toString());
        }
    }

    public abstract void validate() throws UnsupportedException;
    public abstract ProtectionLevel defaultProtectionLevel();
    public abstract ImplementationLevel defaultImplementationLevel();
}