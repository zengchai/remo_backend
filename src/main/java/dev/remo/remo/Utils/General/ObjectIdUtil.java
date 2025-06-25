package dev.remo.remo.Utils.General;

import org.bson.types.ObjectId;

import dev.remo.remo.Utils.Exception.NotFoundResourceException;

public class ObjectIdUtil {

    public static ObjectId validateObjectId(String id) {

        if (id == null || !ObjectId.isValid(id)) {
            throw new NotFoundResourceException("Invalid listing id: " + id);
        }
        
        return new ObjectId(id);
    }
}
