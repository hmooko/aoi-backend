INSERT IGNORE INTO feature (code) VALUES
('AI_PROBLEM');

INSERT IGNORE INTO plan (code) VALUES
('FREE'),
('PREMIUM_MONTH');

INSERT IGNORE INTO plan_feature_quota (plan_id, feature_id, quota)
SELECT
    (SELECT id FROM plan WHERE code = 'FREE'),
    (SELECT id FROM feature WHERE code = 'AI_PROBLEM'),
    10
ON DUPLICATE KEY UPDATE
    quota = VALUES(quota);

INSERT IGNORE INTO plan_feature_quota (plan_id, feature_id, quota)
SELECT
    (SELECT id FROM plan WHERE code = 'PREMIUM_MONTH'),
    (SELECT id FROM feature WHERE code = 'AI_PROBLEM'),
    50
ON DUPLICATE KEY UPDATE
    quota = VALUES(quota);