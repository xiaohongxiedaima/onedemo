package com.xiaohongxiedaima.demo.algorithm.adnf;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * @author liusheng
 * @date A2021年09月01日 4:08 下午
 */
@Slf4j
public class Main {
    public static Map<Integer, String> read(InputStream is) {
        Scanner scanner = new Scanner(is);
        Map<Integer, String> map = new HashMap<>();
        while(scanner.hasNextLine()) {
            String[] kv = scanner.nextLine().split("\\|");
            int adid = Integer.parseInt(kv[0].trim());
            String conjunction = kv[1].trim();
            map.put(adid, conjunction);
        }
        return map;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, String> data = read(new FileInputStream("/Users/xiaohong/repo/github/onedemo/onedemo-algorithm/src/main/resources/dnf.txt"));

        DNF dnf = new DNF();
        data.forEach(dnf::add);
        dnf.print();

        // {age: A30}
        List<Conjunction.Assignment.Term> queryTermList = Arrays.asList(Conjunction.Assignment.Term.of("age", "A30"));
        List<Integer> adidList = dnf.query(queryTermList);
        log.info("queryTermList: {}, adidList: {}", queryTermList, adidList);

        // {age: A20}
        queryTermList = Arrays.asList(Conjunction.Assignment.Term.of("age", "A20"));
        adidList = dnf.query(queryTermList);
        log.info("queryTermList: {}, adidList: {}", queryTermList, adidList);

        // {age:A20, gender: 女}
        queryTermList = Arrays.asList(Conjunction.Assignment.Term.of("age", "A20"), Conjunction.Assignment.Term.of("gender", "女"));
        adidList = dnf.query(queryTermList);
        log.info("queryTermList: {}, adidList: {}", queryTermList, adidList);

        // {age:A20, gender:女, geo:北京}
        queryTermList = Arrays.asList(Conjunction.Assignment.Term.of("age", "A20"), Conjunction.Assignment.Term.of("gender", "女"), Conjunction.Assignment.Term.of("geo", "北京"));
        adidList = dnf.query(queryTermList);
        log.info("queryTermList: {}, adidList: {}", queryTermList, adidList);
    }
}
