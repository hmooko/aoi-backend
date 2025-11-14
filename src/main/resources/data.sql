-- Features: AI 문제 생성 기능
MERGE INTO feature dest
USING (SELECT 'AI_PROBLEM' AS code FROM dual) src
ON (dest.code = src.code)
WHEN NOT MATCHED THEN
  INSERT (code) VALUES (src.code);

-- Plans: 무료, 프리미엄 월간 플랜
MERGE INTO plan dest
USING (SELECT 'FREE' AS code FROM dual) src
ON (dest.code = src.code)
WHEN NOT MATCHED THEN
  INSERT (code) VALUES (src.code);

MERGE INTO plan dest
USING (SELECT 'PREMIUM_MONTH' AS code FROM dual) src
ON (dest.code = src.code)
WHEN NOT MATCHED THEN
  INSERT (code) VALUES (src.code);


-- Plan Feature Quotas: 각 플랜별 기능 사용량 설정
-- 무료 플랜: AI 문제 생성 10회
MERGE INTO plan_feature_quota dest
USING (
  SELECT
    (SELECT id FROM plan WHERE code = 'FREE') AS plan_id,
    (SELECT id FROM feature WHERE code = 'AI_PROBLEM') AS feature_id,
    10 AS quota
  FROM dual
) src
ON (dest.plan_id = src.plan_id AND dest.feature_id = src.feature_id)
WHEN MATCHED THEN
  UPDATE SET dest.quota = src.quota
WHEN NOT MATCHED THEN
  INSERT (plan_id, feature_id, quota) VALUES (src.plan_id, src.feature_id, src.quota);

-- 프리미엄 월간 플랜: AI 문제 생성 50회
MERGE INTO plan_feature_quota dest
USING (
  SELECT
    (SELECT id FROM plan WHERE code = 'PREMIUM_MONTH') AS plan_id,
    (SELECT id FROM feature WHERE code = 'AI_PROBLEM') AS feature_id,
    50 AS quota
  FROM dual
) src
ON (dest.plan_id = src.plan_id AND dest.feature_id = src.feature_id)
WHEN MATCHED THEN
  UPDATE SET dest.quota = src.quota
WHEN NOT MATCHED THEN
  INSERT (plan_id, feature_id, quota) VALUES (src.plan_id, src.feature_id, src.quota);