public enum MedlemsTyp {
    NUVARANDE_MEDLEM("�r en nuvarande medlem"),
    F�REDETTA_MEDLEM("�r en f�re detta medlem"),
    ICKE_MEDLEM("Denna person �r ej medlem.");

    public final String MedlemsTypMeddelande;
    MedlemsTyp(String typ) {
        MedlemsTypMeddelande = typ;
    }
}
