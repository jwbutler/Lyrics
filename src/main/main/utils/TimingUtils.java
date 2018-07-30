package main.utils;

import jdk.nashorn.internal.codegen.CompilerConstants;

import javax.annotation.Nonnull;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author jbutler
 * @since July 2018
 */
public class TimingUtils
{
    public static void logTimingFor(@Nonnull Callable<?> callable)
    {
        Thread t = new Thread(() ->
        {
            for (int i = 0; ; i++)
            {
                System.out.println(i);
            }
        });
        CompletableFuture.runAsync(() ->
        {
            try
            {
                callable.call();
            }
            catch (Throwable e)
            {
                throw new RuntimeException();
            }
        })
            .whenComplete((result, throwable) ->
            {
                try
                {
                    t.join();
                } catch (InterruptedException e)
                {
                }
            });
    }
}
