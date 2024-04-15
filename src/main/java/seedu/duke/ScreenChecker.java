//@@author songyuew
package seedu.duke;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

//@@author songyuew
class ScreenChecker {

    private final Terminal terminal;
    private final int minHeight = 18;
    private final int minWidth = 80;

    ScreenChecker() throws IOException {
        this.terminal = TerminalBuilder.terminal();
    }

    public Boolean checkOptimalSize() {
        int currentWidth = (int) terminal.getWidth();
        int currentHeight = (int) terminal.getHeight();

        return currentWidth >= minWidth && currentHeight >= minHeight;
    }

    public String getCurrentSize() {
        return (int) terminal.getWidth() + "*" + (int) terminal.getHeight();
    }

}
