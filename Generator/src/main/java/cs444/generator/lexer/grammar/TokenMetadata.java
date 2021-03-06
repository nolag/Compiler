package cs444.generator.lexer.grammar;

public final class TokenMetadata {
    public final String name;
    public final int priority;
    public final Type type;

    public TokenMetadata(String name, int priority, Type type) {
        this.name = name;
        this.priority = priority;
        this.type = type;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + priority;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        TokenMetadata other = (TokenMetadata) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        return priority == other.priority;
    }

    public enum Type {VALID, SYNTAX_ONLY, IGNORE}
}
