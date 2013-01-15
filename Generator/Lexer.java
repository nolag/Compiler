public class Lexer {
    private final Token.Factory PlusFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.Plus, lexeme);
        }
    }
    private final Token.Factory MinusFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.Minus, lexeme);
        }
    }
    private final Token.Factory WhiteSpaceFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.WhiteSpace, lexeme);
        }
    }
    private final Token.Factory NumberFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.Number, lexeme);
        }
    }
    private final Token.Factory EqualsFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.Equals, lexeme);
        }
    }
    private final Token.Factory DeclarationFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.Declaration, lexeme);
        }
    }
    private final Token.Factory IdentifierFactory = new Token.Factory {
        @Override
        public Token create(String lexeme) {
            return new Token(Token.Id.Identifier, lexeme);
        }
    }
    private static Token.Factory getFactory(int state) {
        switch (state) {
        case 0: return PlusFactory;
        case 0: return MinusFactory;
        case 0: return WhiteSpaceFactory;
        case 0: return NumberFactory;
        case 0: return EqualsFactory;
        case 0: return DeclarationFactory;
        case 0: return IdentifierFactory;
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
