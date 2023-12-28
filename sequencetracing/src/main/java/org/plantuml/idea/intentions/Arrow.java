package org.plantuml.idea.intentions;

import java.util.Arrays;

public class Arrow {
    private int caretOffset;
    private char[] chars;
    private int[] ends = new int[]{-1, -1};
    /**
     * there are so many types of arrow, lets allow anything which contains - or .
     */
    private boolean containsArrowBody;

    public static Arrow from(int caretOffset, char[] chars) {
        return new Arrow(caretOffset, chars).invoke();
    }

    private Arrow(int caretOffset, char... chars) {
        this.caretOffset = caretOffset;
        this.chars = chars;
    }

    private Arrow invoke() {
        if (caretWithinCharArray()) {
            if (isArrowCharAtCaret()) {
                //traverse right to find the end
                for (int i = caretOffset; i < chars.length; i++) {
                    if (!processPosition(i)) break;
                }
            }
            //traverse left to find the second end
            if (arrowEndsNotFound() && previousCharIsNotWhitespace()) {
                for (int i = caretOffset - 1; i >= 0; i--) {//must step one char left, for cases when we are on far right edge 
                    if (!processPosition(i)) break;
                }
            }
        }
        return this;
    }

    private boolean isArrowCharAtCaret() {
        char currentChar = chars[caretOffset];
        return isArrowChar(currentChar);
    }

    private boolean processPosition(int position) {
        char currentChar = chars[position];
        boolean currentIsArrowChar = isArrowChar(currentChar);

        boolean continueTraversing = currentIsArrowChar; //continue traversing when caret is in middle
        if (currentIsArrowChar && isOnEdge(position)) {
            continueTraversing = savePosition(position);
        }
        if (isArrowBody(currentChar)) {
            containsArrowBody = true;
        }
        return continueTraversing;
    }


    private boolean savePosition(int position) {
        boolean continueTraversing;
        if (ends[0] == -1) {
            ends[0] = position;
            continueTraversing = true;
        } else {
            ends[1] = position;
            Arrays.sort(ends);
            continueTraversing = false;
        }
        return continueTraversing;
    }

    private boolean isArrowBody(char currentChar) {
        return currentChar == '-' || currentChar == '.';
    }

    private boolean isOnEdge(int currentPosition) {
        char charRight = chars.length > currentPosition + 1 ? chars[currentPosition + 1] : ' ';
        char charLeft = currentPosition - 1 >= 0 ? chars[currentPosition - 1] : ' ';
        return isWhitespace(charLeft) || isWhitespace(charRight) || isLetterOrDigitExceptArrowChars(charLeft) || isLetterOrDigitExceptArrowChars(charRight);
    }

    private boolean isLetterOrDigitExceptArrowChars(char c) {
        return Character.isLetterOrDigit(c) && c != 'o' && c != 'x';
    }


    private boolean isArrowChar(char c) {
        return c == 'o' || c == 'x' || (isNotWhitespace(c) && !Character.isLetterOrDigit(c));
    }
    
    
    private boolean isNotWhitespace(char currentChar) {
        return currentChar != ' ';
    }

    private boolean isWhitespace(char currentChar) {
        return currentChar == ' ';
    }

    private boolean arrowEndsNotFound() {
        return ends[0] == -1 || ends[1] == -1;
    }

    private boolean previousCharIsNotWhitespace() {
        return caretOffset > 0 && isNotWhitespace(chars[caretOffset - 1]);
    }

    private boolean caretWithinCharArray() {
        return chars.length > caretOffset;
    }

    public boolean isValid() {
        if (arrowEndsNotFound()) {
            return false;
        }
        if (!containsArrowBody) {
            return false;
        }
        return !(isArrowBody(chars[ends[0]]) && isArrowBody(chars[ends[1]]));
    }

    public int getStart() {
        return ends[0];
    }

    public int getEnd() {
        return ends[1];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Arrow{");
        sb.append("caretOffset=").append(caretOffset);
        sb.append(", chars=").append(Arrays.toString(chars));
        sb.append(", ends=").append(Arrays.toString(ends));
        sb.append(", containsArrowBody=").append(containsArrowBody);
        sb.append('}');
        return sb.toString();
    }
}
