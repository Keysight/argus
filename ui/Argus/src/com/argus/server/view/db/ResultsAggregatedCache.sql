SELECT * FROM 
	PYTHAR.b_runs 
INNER JOIN 
	(
	SELECT 
		b_builds.run_key,
        b_runs.linux_user,
		max(case when product_name = 'IxOs' then build_number end) AS 'IxOs',
		max(case when product_name = 'IxNetwork' then build_number end) AS 'IxNetwork'
	FROM `PYTHAR`.`b_builds` 
        INNER JOIN
    `PYTHAR`.`b_runs` 
    ON b_runs.KEY = b_builds.run_key AND b_runs.linux_user='demopythar'
	GROUP BY run_key
	) b_builds_transposed
ON b_runs.KEY = b_builds_transposed.run_key AND b_runs.linux_user='demopythar' AND b_runs.visible=1;

