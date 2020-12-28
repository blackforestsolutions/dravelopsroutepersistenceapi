package de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

@Service
public class ShaIdServiceImpl implements ShaIdService {

    @Override
    public <T> String generateShaIdWith(T object) throws IOException {
        if (Optional.ofNullable(object).isEmpty()) {
            throw new NullPointerException();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.flush();
        return DigestUtils.sha1Hex(bos.toByteArray());
    }

}
