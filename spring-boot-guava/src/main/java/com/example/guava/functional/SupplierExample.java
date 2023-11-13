package com.example.guava.functional;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class SupplierExample {

    static class Girl {
        int age;
        String face;

        public Girl(int age, String face) {
            this.age = age;
            this.face = face;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }
    }

    @Test
    public void testSupplier() {
        Supplier<Predicate<String>> supplier = new Supplier<Predicate<String>>() {
            @Override
            public Predicate<String> get() {
                Map<String, Girl> map = new HashMap<>();
                map.put("love the face", new Girl(18, "nice"));
                map.put("love the age", new Girl(16, "ugly"));
                Function<String, Girl> function = Functions.forMap(map);
                Predicate<Girl> predicate = girl -> girl.getAge() >= 18;
                Predicate<String> result = Predicates.compose(predicate, function);
                return result;
            }
        };
        System.out.println(supplier.get().apply("love the age"));
        System.out.println(supplier.get().apply("love the face"));
    }

    public static void main(String[] args) {
        Supplier<String> supplier = String::new;
        Class<? extends String> aClass = supplier.get().getClass();
        Class<? extends String> aClass1 = supplier.get().getClass();
        Assert.assertThat(aClass == aClass1, Matchers.equalTo(true));
    }

}
