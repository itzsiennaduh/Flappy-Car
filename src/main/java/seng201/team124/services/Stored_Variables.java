package seng201.team124.services;

public class Stored_Variables {
    private static String CharacterName;

    public static String getCharacterName() {
        return CharacterName;
    }

    public static void setCharacterName(String characterName) {
        CharacterName = characterName;
    }

    private static int difficulty = 2;

    public static void setDifficulty(int diff) {
        difficulty = diff;
    }

    public static int getDifficulty() {
        return difficulty;
    }
}
