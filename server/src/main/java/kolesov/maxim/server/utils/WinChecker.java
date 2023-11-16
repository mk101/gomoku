package kolesov.maxim.server.utils;

import kolesov.maxim.server.model.Color;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WinChecker {

    private static final int WIN_ROW = 5;

    public static boolean check(Color[][] map, int x, int y) {
        Color playerColor = map[x][y];

        if (checkHorizontal(map, x, y, playerColor)) {
            return true;
        }

        if (checkVertical(map, x, y, playerColor)) {
            return true;
        }

        if (checkNE(map, x, y, playerColor)) {
            return true;
        }

        return checkNW(map, x, y, playerColor);
    }

    private static boolean checkHorizontal(Color[][] map, int x, int y, Color playerColor) {
        int left = x;
        int right = x;

        while (left > 0 && map[left - 1][y] == playerColor) {
            left--;
        }

        while (right < map.length - 1 && map[right + 1][y] == playerColor) {
            right++;
        }

        return right - left + 1 >= WIN_ROW;
    }

    private static boolean checkVertical(Color[][] map, int x, int y, Color playerColor) {
        int top = y;
        int bottom = y;

        while (top > 0 && map[x][top - 1] == playerColor) {
            top--;
        }

        while (bottom < map[x].length - 1 && map[x][bottom + 1] == playerColor) {
            bottom++;
        }

        return bottom - top + 1 >= WIN_ROW;
    }

    /*
    x
     x
      x
     */
    private static boolean checkNW(Color[][] map, int x, int y, Color playerColor) {
        int top = y;
        int bottom = y;
        int left = x;
        int right = x;

        while (left > 0 && top > 0 && map[left - 1][top - 1] == playerColor) {
            left--;
            top--;
        }

        while (right < map.length - 1 && bottom < map[x].length - 1 && map[right + 1][bottom + 1] == playerColor) {
            right++;
            bottom++;
        }

        return bottom - top + 1 >= WIN_ROW;
    }


    /*
       x
      x
     x
    */
    private static boolean checkNE(Color[][] map, int x, int y, Color playerColor) {
        int top = y;
        int bottom = y;
        int left = x;
        int right = x;

        while (left > 0 && bottom < map[x].length - 1 && map[left - 1][bottom + 1] == playerColor) {
            left--;
            bottom++;
        }

        while (right < map.length - 1 && top > 0 && map[right + 1][top - 1] == playerColor) {
            right++;
            top--;
        }

        return bottom - top + 1 >= WIN_ROW;
    }

}
