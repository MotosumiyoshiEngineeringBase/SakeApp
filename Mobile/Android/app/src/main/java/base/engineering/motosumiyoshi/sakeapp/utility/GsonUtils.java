package base.engineering.motosumiyoshi.sakeapp.utility;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    public static <T> T objectConvertFromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> List<T> objectListConvertFromJson(String json, Class<T> clazz) {
        Type type = $Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, clazz);
        return new Gson().fromJson(json, type);
    }
}
