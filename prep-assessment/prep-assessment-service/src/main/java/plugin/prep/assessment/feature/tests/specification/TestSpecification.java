package plugin.prep.assessment.feature.tests.specification;

import org.springframework.data.jpa.domain.*;

import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.enums.*;

public class TestSpecification {

    public static Specification<TestEntity> hasType(TypeTestEnum type) {
        return (root, query, cb) ->
            cb.equal(root.get("type"), type);
    }

    public static Specification<TestEntity> hasGrade(TestGradeEnum grade) {
        return (root, query, cb) ->
            cb.equal(root.get("grade"), grade);
    }

    public static Specification<TestEntity> hasTopicId(Long topicId) {
        return (root, query, cb) ->
            cb.equal(root.get("topic").get("id"), topicId);
    }
}
