package cs444.codegen.x86.x86_64;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_64.tiles.helpers.X86_64TileHelper;
import cs444.codegen.x86.x86_64.tiles.helpers.X86_64TileInit;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public abstract class X86_64Platform extends X86Platform {
    public final X86SelectorIndexedTable sit;

    private static X86Instruction [] directives = { Default.rel };

    protected X86_64Platform(final IRuntime<X86Instruction> runtime, final Set<String> opts){
        super(opts, X86_64TileInit.instance, runtime, X86SizeHelper.sizeHelper64);
        sit = new X86SelectorIndexedTable(sizeHelper);
    }

    @Override
    public final X86ObjectLayout getObjectLayout() {
        return X86ObjectLayout.object64;
    }

    @Override
    public final X86SelectorIndexedTable getSelectorIndex() {
        return sit;
    }

    @Override
    public final void generateStaticCode(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException {

        StaticFieldInit.generateCode(resolvers, this, outputFile, directory);
    }

    @Override
    public final void genInstructorInvoke(final APkgClassResolver resolver, final Addable<X86Instruction> instructions) {
        X86_64TileHelper.instance.invokeConstructor(resolver, Collections.<ISymbol>emptyList(), this, instructions);
    }

    @Override
    public final void moveStaticLong(final String staticLbl, final Addable<X86Instruction> instructions){
        final Immediate lbl = new Immediate(staticLbl);
        final Memory toAddr = new Memory(lbl);
        instructions.add(new Mov(toAddr, Register.ACCUMULATOR, sizeHelper));
    }

    @Override
    public final void zeroStaticLong(final String staticLbl, final Addable<X86Instruction> instructions){
        final Immediate lbl = new Immediate(staticLbl);
        final Memory toAddr = new Memory(lbl);
        instructions.add(new Mov(toAddr, Immediate.ZERO, sizeHelper));
    }

    @Override
    public void genHeaderEnd(final APkgClassResolver resolver, final Addable<X86Instruction> instructions) {
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Comment("Store pointer to object in ebx"));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

        for (final DclSymbol fieldDcl : resolver.getUninheritedNonStaticFields()) {
            final Size size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));

            final Memory fieldAddr = new Memory(Register.BASE, fieldDcl.getOffset(this));

            if(!fieldDcl.children.isEmpty()){
                instructions.add(new Comment("Initializing field " + fieldDcl.dclName + "."));

                final CodeGenVisitor<X86Instruction, Size> visitor = new CodeGenVisitor<X86Instruction, Size>(
                        CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(this).currentFile, this);

                final ISymbol field = fieldDcl.children.get(0);
                field.accept(visitor);
                instructions.addAll(getBest(field));

                instructions.add(new Mov(fieldAddr, Register.ACCUMULATOR, size, sizeHelper));
            }
        }
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(Ret.RET);
    }

    @Override
    public final TileSet<X86Instruction, Size> getTiles(){
        return TileSet.<X86Instruction, Size>getOrMake(X86_64Platform.class);
    }

    @Override
    public X86Instruction[] getAssemblerDirectives() {
        return directives;
    }

    @Override
    public TileHelper<X86Instruction, Size> getTileHelper() {
        return X86_64TileHelper.instance;
    }
}