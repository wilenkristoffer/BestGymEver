public enum MedlemsTyp {
    NUVARANDE_MEDLEM("är en nuvarande medlem"),
    FÖREDETTA_MEDLEM("är en före detta medlem"),
    ICKE_MEDLEM("Denna person är ej medlem.");

    public final String MedlemsTypMeddelande;
    MedlemsTyp(String typ) {
        MedlemsTypMeddelande = typ;
    }
}
