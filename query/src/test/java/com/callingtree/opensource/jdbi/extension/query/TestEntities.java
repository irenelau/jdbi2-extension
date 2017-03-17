package com.callingtree.opensource.jdbi.extension.query;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TestEntities {

    public static class Person {

        private Integer id;

        public Integer getId() {
            return id;
        }

        public Person setId(Integer id) {
            this.id = id;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            return new EqualsBuilder()
                    .append(id, person.id)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(id)
                    .toHashCode();
        }
    }

    public static class Nickname {

        private Integer id;

        private String nickname;

        public Integer getId() {
            return id;
        }

        public Nickname setId(Integer id) {
            this.id = id;
            return this;
        }

        public String getNickname() {
            return nickname;
        }

        public Nickname setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Nickname nickname1 = (Nickname) o;

            return new EqualsBuilder()
                    .append(id, nickname1.id)
                    .append(nickname, nickname1.nickname)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(id)
                    .append(nickname)
                    .toHashCode();
        }
    }

    public static class Phone {

        private Integer id;

        private TestEnums.PhoneType type;

        private Long number;

        private Boolean isActive;

        public Integer getId() {
            return id;
        }

        public Phone setId(Integer id) {
            this.id = id;
            return this;
        }

        public TestEnums.PhoneType getType() {
            return type;
        }

        public Phone setType(TestEnums.PhoneType type) {
            this.type = type;
            return this;
        }

        public Long getNumber() {
            return number;
        }

        public Phone setNumber(Long number) {
            this.number = number;
            return this;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public Phone setIsActive(Boolean active) {
            isActive = active;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Phone phone = (Phone) o;

            return new EqualsBuilder()
                    .append(id, phone.id)
                    .append(type, phone.type)
                    .append(number, phone.number)
                    .append(isActive, phone.isActive)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(id)
                    .append(type)
                    .append(number)
                    .append(isActive)
                    .toHashCode();
        }
    }
}
