package com.harrykay.smartgolems.server.command;

public class Constants {

    /* ROOT COMMAND */

    /* SELECTOR COMMANDS */

    public static final String ALL_COMMAND = "all";
    public static final String GOLEM_COMMAND = "golem";
    public static final String GROUP = "group";

    /* SUB COMMANDS */

    public static final String BUILD_COMMAND = "build";
    public static final String CHARGE_COMMAND = "charge";
    public static final String DO_CREEPER_THINGS_COMMAND = "do-creeper-things";
    public static final String DO_IRON_GOLEM_THINGS_COMMAND = "do-iron-golem-things";
    public static final String SUICIDE_COMMAND = "suicide";
    public static final String MOVE_TO_COMMAND = "move-to";
    public static final String MOVE_TO_AND_STAY_COMMAND = "move-to-and-stay";
    public static final String FORGET_COMMAND = "forget";
    public static final String HALT_COMMAND = "halt";
    public static final String PLACE_BLOCK_COMMAND = "place-block";
    public static final String SHIFT_PRIORITIES_COMMAND = "shift-priorities";
    public static final String SHOW_GOALS_COMMAND = "show-goeals";
    public static final String SWAP_PRIORITIES_COMMAND = "swap-priorities";
    public static final String MINE_COMMAND = "mine";
    public static final String WOOD_CUT_COMMAND = "wood-cut";
    public static final String FISH_COMMAND = "fish";
    public static final String HUNT_COMMAND = "hunt";
    public static final String ATTACK_COMMAND = "attack";
    public static final String DIG_COMMAND = "dig";
    public static final String ADD_TO_GROUP_COMMAND = "add-to-group";
    public static final String REMOVE_FROM_GROUP_COMMAND = "remove-from-group";
    public static final String FOLLOW_COMMAND = "follow";
    public static final String RENAME_COMMAND = "rename";

    /* ARGUMENTS */

    public static final String GOLEM_NAME_ARG = "golem name";
    public static final String PLAYER_NAME_ARG = "player name";
    public static final String PRIORITY_ARG = "priority";
    public static final String POSITION_ARG = "(x,y,z)";
    public static final String BLOCK_ARG = "block";
    public static final String SHIFT_PRIORITY_AMOUNT_ARG = "amount";
    public static final String PRIORITIY_FROM_ARG = "prioritiy from";
    public static final String PRIORITIY_WITH_ARG = "priority with";

    /* NOTIFICATIONS */

    public static final String GOLEM_NOT_FOUND = "That golem could not be found.";
    public static final String PLAYER_NOT_FOUND = "That player could not be found.";
    public static final String PRIORITY_TO_GOAL_KEY = "\"Priority : task\"";

}
