package com.upgrad.FoodOrderingApp.service.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class ItemType {

    public enum ItemTypeEnum {
        VEG("VEG"),

        NON_VEG("NON_VEG");

        private String value;

        ItemTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ItemTypeEnum fromValue(String text) {
            for (ItemTypeEnum b : ItemTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

}
