UPDATE route r
SET r.name = '1호선'
WHERE r.name IN ('경부선', '경인선', '경원선', '장항선');
UPDATE route r
SET r.name = '신분당선'
WHERE r.name IN ('신분당선(연장)', '신분당선(연장2)');
UPDATE route r
SET r.name = '9호선'
WHERE r.name IN ('9호선(연장)');
UPDATE route r
SET r.name = '7호선'
WHERE r.name IN ('7호선(인천)');
UPDATE route r
SET r.name = '수인분당선'
WHERE r.name IN ('수인선', '분당선');
UPDATE route r
SET r.name = 'GTX'
WHERE r.name IN ('수도권 광역급행철도');
UPDATE route r
SET r.name = '공항철도'
WHERE r.name IN ('공항철도1호선');

