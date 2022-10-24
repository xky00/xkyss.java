package com.xkyss.json.spi;


import com.xkyss.core.util.Servicex;
import com.xkyss.json.jackson.JacksonFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A factory for the plug-able json SPI.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@SuppressWarnings("ALL")
public interface JsonFactory {

    /**
     * <p> Load the JSON factory with the {@code ServiceLoader}
     *
     * <ul>
     *   <li>An attempt is made to load a factory using the service loader {@code META-INF/services} {@link JsonFactory}.</li>
     *   <li>Factories are sorted </li>
     *   <li>If not factory is resolved (which is usually the default case), {@link JacksonFactory#INSTANCE} is used.</li>
     * </ul>
     *
     * <p> When the default Jackson codec is used and {@code jackson-databind} is available then a codec using it
     * will be used otherwise the codec will only use {@code jackson-core} and provide best effort mapping.
     */
    static JsonFactory load() {
        List<JsonFactory> factories = new ArrayList<>(Servicex.loadFactories(JsonFactory.class));
        factories.sort(Comparator.comparingInt(JsonFactory::order));
        if (factories.size() > 0) {
            return factories.iterator().next();
        } else {
            return JacksonFactory.INSTANCE;
        }
    }

    /**
     * The order of the factory. If there is more than one matching factory they will be tried in ascending order.
     *
     * @implSpec returns {@link Integer#MAX_VALUE}
     *
     * @return  the order
     */
    default int order() {
        return Integer.MAX_VALUE;
    }

    JsonCodec codec();

}
