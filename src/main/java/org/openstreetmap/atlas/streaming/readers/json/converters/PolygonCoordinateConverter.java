package org.openstreetmap.atlas.streaming.readers.json.converters;

import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.atlas.geography.Location;
import org.openstreetmap.atlas.geography.Polygon;
import org.openstreetmap.atlas.utilities.conversion.Converter;

import com.google.gson.JsonArray;

/**
 * Used for {@link Polygon}s
 *
 * @author matthieun
 */
public class PolygonCoordinateConverter implements Converter<Iterable<Location>, JsonArray>
{
    private final PointCoordinateConverter coordinateConverter = new PointCoordinateConverter();

    @Override
    public JsonArray convert(final Iterable<Location> object)
    {
        final JsonArray result = new JsonArray();
        final JsonArray inner = new JsonArray();
        object.forEach(location ->
        {
            inner.add(this.coordinateConverter.convert(location));
        });
        result.add(inner);
        return result;
    }

    public Converter<JsonArray, List<Location>> revert()
    {
        return jsonArray ->
        {
            final List<Location> result = new ArrayList<>();
            jsonArray.forEach(jsonElement ->
            {
                final JsonArray array = (JsonArray) jsonElement;
                array.forEach(element ->
                {
                    final JsonArray array2 = (JsonArray) element;
                    result.add(this.coordinateConverter.revert().convert(array2));
                });
            });
            return result;
        };
    }
}
