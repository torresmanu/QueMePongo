package que_me_pongo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.awt.*;

@Converter
public class ColoresAttributeConverter implements AttributeConverter<Color, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Color color) {
        return color == null? null : color.getRGB();
    }

    @Override
    public Color convertToEntityAttribute(Integer rgb) {
        return rgb == null? null : new Color(rgb);
    }

}