package com.ataya.company.enums;

public enum SocialMediaPlatforms {
    FACEBOOK,
    TWITTER,
    INSTAGRAM,
    LINKEDIN,
    YOUTUBE,
    PINTEREST,
    TIKTOK,
    SNAPCHAT,
    WHATSAPP,
    TELEGRAM,
    VIBER,
    WECHAT,
    WEIBO,
    REDDIT,
    TUMBLR,
    VKONTAKTE,
    TINDER,
    BUMBLE,
    GRINDR,
    OKCUPID,
    MATCH,
    HINGE,
    ZOOSK,
    BADO,
    ELITESINGLES,
    SILVERSINGLES,
    OURTIME,
    PARSHIP,
    JDATE,
    JSWIPE;

    // check if the platform exist ignore case
    public static boolean isPlatformExists(String platform) {
        for (SocialMediaPlatforms p : SocialMediaPlatforms.values()) {
            if (p.name().equalsIgnoreCase(platform)) {
                return true;
            }
        }
        return false;
    }

    // to get the platform by name
    public static SocialMediaPlatforms getPlatform(String platform) {
        for (SocialMediaPlatforms p : SocialMediaPlatforms.values()) {
            if (p.name().equalsIgnoreCase(platform)) {
                return p;
            }
        }
        return null;
    }

    public static boolean isPlatformExists(SocialMediaPlatforms platform) {
        for (SocialMediaPlatforms p : SocialMediaPlatforms.values()) {
            if (p.equals(platform)) {
                return true;
            }
        }
        return false;
    }
}
