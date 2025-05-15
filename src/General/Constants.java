package General;

public class Constants {
    // Kolory ANSI
    public static final String RESET_COLOR = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String CYAN = "\033[36m";

    // Ogólne stałe
    public static final int SPREAD_PROBABILITY = 5; // %

    // Human
    public static final int HUMAN_STRENGTH = 5;
    public static final int HUMAN_INITIATIVE = 4;
    public static final String HUMAN_GRAPHIC_REPRESENTATION = RED + "H" + RESET_COLOR;
    public static final int HUMAN_SPECIAL_MOVE_COOLDOWN = 5;

    // Animals
    public static final int WOLF_STRENGTH = 9;
    public static final int WOLF_INITIATIVE = 5;
    public static final String WOLF_GRAPHIC_REPRESENTATION = RED + "W" + RESET_COLOR;

    public static final int SHEEP_STRENGTH = 4;
    public static final int SHEEP_INITIATIVE = 4;
    public static final String SHEEP_GRAPHIC_REPRESENTATION = "S";

    public static final int FOX_STRENGTH = 3;
    public static final int FOX_INITIATIVE = 7;
    public static final String FOX_GRAPHIC_REPRESENTATION = YELLOW + "F" + RESET_COLOR;

    public static final int TURTLE_STRENGTH = 2;
    public static final int TURTLE_INITIATIVE = 1;
    public static final String TURTLE_GRAPHIC_REPRESENTATION = GREEN + "T" + RESET_COLOR;
    public static final int TURTLE_MOVE_CHANCE = 25; // %
    public static final int TURTLE_DEFLECT_POWER = 10;

    public static final int ANTYLOPE_STRENGTH = 4;
    public static final int ANTYLOPE_INITIATIVE = 4;
    public static final String ANTYLOPE_GRAPHIC_REPRESENTATION = CYAN + "A" + RESET_COLOR;
    public static final int ANTYLOPE_MOVE_RANGE = 2;
    public static final int ANTYLOPE_ESCAPE_CHANCE = 50; // %

    public static final int CYBER_SHEEP_STRENGTH = 10;
    public static final int CYBER_SHEEP_INITIATIVE = 4;
    public static final String CYBER_SHEEP_GRAPHIC_REPRESENTATION = MAGENTA + "C" + RESET_COLOR;

    // Plants
    public static final String GRASS_GRAPHIC_REPRESENTATION = GREEN + "." + RESET_COLOR;
    public static final int GRASS_STRENGTH = 0;

    public static final String MILKWEED_GRAPHIC_REPRESENTATION = YELLOW + "*" + RESET_COLOR;
    public static final int MILKWEED_STRENGTH = 0;
    public static final int SPREAD_ATTEMPTS = 3;

    public static final int GUARANA_STRENGTH = 0;
    public static final String GUARANA_GRAPHIC_REPRESENTATION = CYAN + "+" + RESET_COLOR;
    public static final int GUARANA_STRENGTH_BUFF = 3;

    public static final int NIGHTSHADE_BERRIES_STRENGTH = 99;
    public static final String NIGHTSHADE_BERRIES_GRAPHIC_REPRESENTATION = MAGENTA + "X" + RESET_COLOR;

    public static final int HOGEWEED_OF_PINE_STRENGTH = 10;
    public static final String HOGEWEED_OF_PINE_GRAPHIC_REPRESENTATION = "#";
}