package miau.hxppytwitch.utils;

import java.awt.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class HxppyUtils {
    public static Color embedColor = new Color(0xBFABFF);
    public static String getCurrentDiscordTime() {
        ZonedDateTime timeNow = ZonedDateTime.now(ZoneId.of("UTC"));
        long timeId = timeNow.toEpochSecond();
        return "<t:"+timeId+":R>";
    }
}
