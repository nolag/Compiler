public class Lexer {
    private final Token.Factory GEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.GE, lexeme);
        }
    }
    private final Token.Factory LTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.LT, lexeme);
        }
    }
    private final Token.Factory STARFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.STAR, lexeme);
        }
    }
    private final Token.Factory RBRACEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.RBRACE, lexeme);
        }
    }
    private final Token.Factory LBRACEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.LBRACE, lexeme);
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
    private final Token.Factory WHITESPACEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.WHITESPACE, lexeme);
        }
    }
    private final Token.Factory MINUSFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.MINUS, lexeme);
        }
    }
    private final Token.Factory IDFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.ID, lexeme);
        }
    }
    private final Token.Factory SEMIFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.SEMI, lexeme);
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
    private final Token.Factory SLASHFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.SLASH, lexeme);
        }
    }
    private final Token.Factory EXCLAMATIONFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.EXCLAMATION, lexeme);
        }
    }
    private final Token.Factory COMMAFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.COMMA, lexeme);
        }
    }
    private final Token.Factory GTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.GT, lexeme);
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
    private final Token.Factory PCTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.PCT, lexeme);
        }
    }
    private final Token.Factory RBRACKETFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.RBRACKET, lexeme);
        }
    }
    private final Token.Factory EQFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.EQ, lexeme);
        }
    }
    private final Token.Factory DOTFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.DOT, lexeme);
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
    private final Token.Factory NEFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.NE, lexeme);
        }
    }
    private static Token.Factory getFactory(int state) {
        switch (state) {
        case 0: return GEFactory;
        case 0: return LTFactory;
        case 0: return STARFactory;
        case 0: return RBRACEFactory;
        case 0: return LBRACEFactory;
        case 0: return BECOMESFactory;
        case 0: return AMPERSANDFactory;
        case 0: return WHITESPACEFactory;
        case 0: return MINUSFactory;
        case 0: return IDFactory;
        case 0: return SEMIFactory;
        case 0: return LPARENFactory;
        case 0: return LBRACKETFactory;
        case 0: return RPARENFactory;
        case 0: return SLASHFactory;
        case 0: return EXCLAMATIONFactory;
        case 0: return COMMAFactory;
        case 0: return GTFactory;
        case 0: return DAMPERSANDFactory;
        case 0: return PIPEFactory;
        case 0: return PLUSFactory;
        case 0: return PCTFactory;
        case 0: return RBRACKETFactory;
        case 0: return EQFactory;
        case 0: return DOTFactory;
        case 0: return DPIPEFactory;
        case 0: return LEFactory;
        case 0: return NEFactory;
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
