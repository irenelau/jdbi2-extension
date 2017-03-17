package com.callingtree.opensource.jdbi.extension.query;

public class TestEnums {

    public enum PhoneType {

        Home,
        Mobile;

        public static PhoneType toPhoneType(String value) {
            if (value != null) {
                return PhoneType.valueOf(value);
            }
            return null;
        }
    }

}
