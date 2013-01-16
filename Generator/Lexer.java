public class Lexer {
    private final Token.Factory PACKAGEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.PACKAGE, lexeme);
        }
    }
    private final Token.Factory PROTECTEDFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.PROTECTED, lexeme);
        }
    }
    private final Token.Factory LTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.LT, lexeme);
        }
    }
    private final Token.Factory CLASSFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.CLASS, lexeme);
        }
    }
    private final Token.Factory STARFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.STAR, lexeme);
        }
    }
    private final Token.Factory WHILEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.WHILE, lexeme);
        }
    }
    private final Token.Factory CONSTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.CONST, lexeme);
        }
    }
    private final Token.Factory LBRACEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.LBRACE, lexeme);
        }
    }
    private final Token.Factory CASEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.CASE, lexeme);
        }
    }
    private final Token.Factory NEWFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.NEW, lexeme);
        }
    }
    private final Token.Factory CHARFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.CHAR, lexeme);
        }
    }
    private final Token.Factory DOFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.DO, lexeme);
        }
    }
    private final Token.Factory FORFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.FOR, lexeme);
        }
    }
    private final Token.Factory FLOATFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.FLOAT, lexeme);
        }
    }
    private final Token.Factory ABSTRACTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.ABSTRACT, lexeme);
        }
    }
    private final Token.Factory IDFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.ID, lexeme);
        }
    }
    private final Token.Factory BREAKFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.BREAK, lexeme);
        }
    }
    private final Token.Factory LPARENFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.LPAREN, lexeme);
        }
    }
    private final Token.Factory LBRACKETFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.LBRACKET, lexeme);
        }
    }
    private final Token.Factory RPARENFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.RPAREN, lexeme);
        }
    }
    private final Token.Factory FINALFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.FINAL, lexeme);
        }
    }
    private final Token.Factory IMPORTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.IMPORT, lexeme);
        }
    }
    private final Token.Factory SLASHFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.SLASH, lexeme);
        }
    }
    private final Token.Factory BOOLEANFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.BOOLEAN, lexeme);
        }
    }
    private final Token.Factory SYNCHRONIZEDFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.SYNCHRONIZED, lexeme);
        }
    }
    private final Token.Factory EXCLAMATIONFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.EXCLAMATION, lexeme);
        }
    }
    private final Token.Factory IMPLEMENTSFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.IMPLEMENTS, lexeme);
        }
    }
    private final Token.Factory CONTINUEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.CONTINUE, lexeme);
        }
    }
    private final Token.Factory COMMAFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.COMMA, lexeme);
        }
    }
    private final Token.Factory TRANSIENTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.TRANSIENT, lexeme);
        }
    }
    private final Token.Factory THISFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.THIS, lexeme);
        }
    }
    private final Token.Factory RETURNFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.RETURN, lexeme);
        }
    }
    private final Token.Factory DOUBLEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.DOUBLE, lexeme);
        }
    }
    private final Token.Factory DAMPERSANDFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.DAMPERSAND, lexeme);
        }
    }
    private final Token.Factory PIPEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.PIPE, lexeme);
        }
    }
    private final Token.Factory PLUSFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.PLUS, lexeme);
        }
    }
    private final Token.Factory END_LINE_COMMENTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.END_LINE_COMMENT, lexeme);
        }
    }
    private final Token.Factory VOIDFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.VOID, lexeme);
        }
    }
    private final Token.Factory SUPERFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.SUPER, lexeme);
        }
    }
    private final Token.Factory EQFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.EQ, lexeme);
        }
    }
    private final Token.Factory RBRACKETFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.RBRACKET, lexeme);
        }
    }
    private final Token.Factory GOTOFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.GOTO, lexeme);
        }
    }
    private final Token.Factory DOTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.DOT, lexeme);
        }
    }
    private final Token.Factory COMMENTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.COMMENT, lexeme);
        }
    }
    private final Token.Factory NEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.NE, lexeme);
        }
    }
    private final Token.Factory GEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.GE, lexeme);
        }
    }
    private final Token.Factory BYTEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.BYTE, lexeme);
        }
    }
    private final Token.Factory RBRACEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.RBRACE, lexeme);
        }
    }
    private final Token.Factory VOLATILEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.VOLATILE, lexeme);
        }
    }
    private final Token.Factory PRIVATEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.PRIVATE, lexeme);
        }
    }
    private final Token.Factory STATICFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.STATIC, lexeme);
        }
    }
    private final Token.Factory SWITCHFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.SWITCH, lexeme);
        }
    }
    private final Token.Factory NULLFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.NULL, lexeme);
        }
    }
    private final Token.Factory DEFAULTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.DEFAULT, lexeme);
        }
    }
    private final Token.Factory ELSEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.ELSE, lexeme);
        }
    }
    private final Token.Factory STRICTFPFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.STRICTFP, lexeme);
        }
    }
    private final Token.Factory BECOMESFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.BECOMES, lexeme);
        }
    }
    private final Token.Factory AMPERSANDFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.AMPERSAND, lexeme);
        }
    }
    private final Token.Factory NATIVEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.NATIVE, lexeme);
        }
    }
    private final Token.Factory WHITESPACEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.WHITESPACE, lexeme);
        }
    }
    private final Token.Factory THROWSFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.THROWS, lexeme);
        }
    }
    private final Token.Factory SHORTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.SHORT, lexeme);
        }
    }
    private final Token.Factory INTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.INT, lexeme);
        }
    }
    private final Token.Factory INSTANCEOFFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.INSTANCEOF, lexeme);
        }
    }
    private final Token.Factory MINUSFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.MINUS, lexeme);
        }
    }
    private final Token.Factory SEMIFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.SEMI, lexeme);
        }
    }
    private final Token.Factory TRUEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.TRUE, lexeme);
        }
    }
    private final Token.Factory ASSERTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.ASSERT, lexeme);
        }
    }
    private final Token.Factory TRYFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.TRY, lexeme);
        }
    }
    private final Token.Factory ENUMFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.ENUM, lexeme);
        }
    }
    private final Token.Factory FINALLYFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.FINALLY, lexeme);
        }
    }
    private final Token.Factory GTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.GT, lexeme);
        }
    }
    private final Token.Factory INTERFACEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.INTERFACE, lexeme);
        }
    }
    private final Token.Factory PCTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.PCT, lexeme);
        }
    }
    private final Token.Factory DECIMAL_INTEGER_LITERALFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.DECIMAL_INTEGER_LITERAL, lexeme);
        }
    }
    private final Token.Factory CATCHFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.CATCH, lexeme);
        }
    }
    private final Token.Factory FALSEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.FALSE, lexeme);
        }
    }
    private final Token.Factory LONGFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.LONG, lexeme);
        }
    }
    private final Token.Factory PUBLICFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.PUBLIC, lexeme);
        }
    }
    private final Token.Factory EXTENDSFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.EXTENDS, lexeme);
        }
    }
    private final Token.Factory DPIPEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.DPIPE, lexeme);
        }
    }
    private final Token.Factory LEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.LE, lexeme);
        }
    }
    private final Token.Factory THROWFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.THROW, lexeme);
        }
    }
    private static Token.Factory getFactory(int state) {
        switch (state) {
        case 0: return PACKAGEFactory;
        case 0: return PROTECTEDFactory;
        case 0: return LTFactory;
        case 0: return CLASSFactory;
        case 0: return STARFactory;
        case 0: return WHILEFactory;
        case 0: return CONSTFactory;
        case 0: return LBRACEFactory;
        case 0: return CASEFactory;
        case 0: return NEWFactory;
        case 0: return CHARFactory;
        case 0: return DOFactory;
        case 0: return FORFactory;
        case 0: return FLOATFactory;
        case 0: return ABSTRACTFactory;
        case 0: return IDFactory;
        case 0: return BREAKFactory;
        case 0: return LPARENFactory;
        case 0: return LBRACKETFactory;
        case 0: return RPARENFactory;
        case 0: return FINALFactory;
        case 0: return IMPORTFactory;
        case 0: return SLASHFactory;
        case 0: return BOOLEANFactory;
        case 0: return SYNCHRONIZEDFactory;
        case 0: return EXCLAMATIONFactory;
        case 0: return IMPLEMENTSFactory;
        case 0: return CONTINUEFactory;
        case 0: return COMMAFactory;
        case 0: return TRANSIENTFactory;
        case 0: return THISFactory;
        case 0: return RETURNFactory;
        case 0: return DOUBLEFactory;
        case 0: return DAMPERSANDFactory;
        case 0: return PIPEFactory;
        case 0: return PLUSFactory;
        case 0: return END_LINE_COMMENTFactory;
        case 0: return VOIDFactory;
        case 0: return SUPERFactory;
        case 0: return EQFactory;
        case 0: return RBRACKETFactory;
        case 0: return GOTOFactory;
        case 0: return DOTFactory;
        case 0: return COMMENTFactory;
        case 0: return NEFactory;
        case 0: return GEFactory;
        case 0: return BYTEFactory;
        case 0: return RBRACEFactory;
        case 0: return VOLATILEFactory;
        case 0: return PRIVATEFactory;
        case 0: return STATICFactory;
        case 0: return SWITCHFactory;
        case 0: return NULLFactory;
        case 0: return DEFAULTFactory;
        case 0: return ELSEFactory;
        case 0: return STRICTFPFactory;
        case 0: return BECOMESFactory;
        case 0: return AMPERSANDFactory;
        case 0: return NATIVEFactory;
        case 0: return WHITESPACEFactory;
        case 0: return THROWSFactory;
        case 0: return SHORTFactory;
        case 0: return INTFactory;
        case 0: return INSTANCEOFFactory;
        case 0: return MINUSFactory;
        case 0: return SEMIFactory;
        case 0: return TRUEFactory;
        case 0: return ASSERTFactory;
        case 0: return TRYFactory;
        case 0: return ENUMFactory;
        case 0: return FINALLYFactory;
        case 0: return GTFactory;
        case 0: return INTERFACEFactory;
        case 0: return PCTFactory;
        case 0: return DECIMAL_INTEGER_LITERALFactory;
        case 0: return CATCHFactory;
        case 0: return FALSEFactory;
        case 0: return LONGFactory;
        case 0: return PUBLICFactory;
        case 0: return EXTENDSFactory;
        case 0: return DPIPEFactory;
        case 0: return LEFactory;
        case 0: return THROWFactory;
        }
        return null;
    }
    private final boolean[] acceptTable =
    private final int[][] stateTable =
    private final int initialState;
    private final Reader reader;
    private int nextChar;
    public Lexer(Reader reader) throws IOException {
        this.reader = reader;
        nextChar = reader.read();
        initialState = 0;
    }
    public Token getNextToken() throws Exception {
        String lexeme = "";
        int state = initialState;
        while (nextChar != -1) {
            int previewState = stateTable[state][nextChar];
            if (previewState == -1) {
                if (acceptTable[state])
                    return new Token(Token.Id.Declaration, lexeme);
                else
                    throw new Exception("Lexer Error");
            } else {
                lexeme += (char)nextChar;
                nextChar = reader.read();
                state = previewState;
            }
        }
        if (acceptTable[state])
            return new Token(Token.Id.Declaration, lexeme);
        return null;
    }
}
