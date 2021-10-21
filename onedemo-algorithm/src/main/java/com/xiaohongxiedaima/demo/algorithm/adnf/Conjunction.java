package com.xiaohongxiedaima.demo.algorithm.adnf;

import com.google.gson.*;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liusheng
 * @date 2021年08月30日 5:42 下午
 *
 * {"age" : ["20", "30"] , "gender" : ["女"] , "geo" : ["北京"]}
 */
@Data
public class Conjunction {
    private List<Assignment> assignmentList = new ArrayList<>();

    private int size;

    public void addAssignment(Assignment assignment) {
        this.assignmentList.add(assignment);
        this.size ++;
    }

    public List<Assignment.Term> toTermList() {
        return this.assignmentList.stream()
                .flatMap(assignment -> assignment.toTermList().stream())
                .collect(Collectors.toList());
    }

    /**
     * {"age" : ["20", "30"]}
     */
    public static class Assignment {
        private final String termKey;

        private final List<String> termValueList;

        public Assignment(String termKey, List<String> termValueList) {
            this.termKey = termKey;
            this.termValueList = termValueList;
        }

        public List<Term> toTermList() {
            return this.termValueList.stream().map(temValue -> Term.of(termKey, temValue)).collect(Collectors.toList());
        }

        /**
         * {age: 20}
         */
        @Data
        public static class Term {
            private String key;

            private String value;

            public static Term of(String key, String value) {
                Term term = new Term();
                term.setKey(key);
                term.setValue(value);
                return term;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Term term = (Term) o;
                return key.equals(term.key) && value.equals(term.value);
            }

            @Override
            public int hashCode() {
                return Objects.hash(key, value);
            }

            @Override
            public String toString() {
                return this.key + " = " + this.value;
            }
        }
    }

    private static final Gson GSON = new Gson();

    /**
     * 将定向条件解析为 Conjunction Assignment Term 结构
     * @param json {"age": ["30"], "geo": ["北京"], "gender": ["男", "女"]}
     * @return
     */
    public static Conjunction parse(String json) {
        Gson GSON = new GsonBuilder()
                .registerTypeAdapter(Conjunction.class, new JsonDeserializer<Conjunction>() {
                    @Override
                    public Conjunction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        Conjunction conjunction = new Conjunction();
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        jsonObject.entrySet().forEach(entry -> {
                            String termKey = entry.getKey();
                            List<String> termValueList = new ArrayList<>();
                            entry.getValue().getAsJsonArray().iterator().forEachRemaining(x -> termValueList.add(x.getAsString()));

                            Assignment assignment = new Assignment(termKey, termValueList);

                            conjunction.addAssignment(assignment);
                        });

                        return conjunction;
                    }
                }).create();

        return GSON.fromJson(json, Conjunction.class);
    }


}
