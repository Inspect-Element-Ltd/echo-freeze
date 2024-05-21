package ac.echo.api.command.sub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommandData {
    String command();
    String desc() default "";
    String usage() default "";
    String permission() default "";
    String[] alias() default "";
}
