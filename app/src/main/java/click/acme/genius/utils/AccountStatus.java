package click.acme.genius.utils;

/**
 * Enumérateur pour gérer le statut d'un compte
 */
public enum AccountStatus {
    NORMAL(1),
    PREMIUM(2),
    MODERATOR(3),
    PROFESSOR(4);

    private int mValue;

    AccountStatus(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
