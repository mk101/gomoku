package kolesov.maxim.common.config;

import kolesov.maxim.common.context.ApplicationContext;
import lombok.Getter;

@Getter
public class GameConfig {

    private final int fieldWidth;
    private final int fieldHeight;

    public GameConfig() {
        ApplicationContext context = ApplicationContext.get();

        fieldWidth = Integer.parseInt(context.getProperty("field.width"));
        fieldHeight = Integer.parseInt(context.getProperty("field.height"));
    }

}
