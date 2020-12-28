package de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice;

import java.io.IOException;

public interface ShaIdService {
    <T> String generateShaIdWith(T object) throws IOException;
}
