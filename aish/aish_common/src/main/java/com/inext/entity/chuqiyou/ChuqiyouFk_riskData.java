package com.inext.entity.chuqiyou;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChuqiyouFk_riskData {

    private String mobile;

    private String gender;

    private String idNum;

    private String apply_time;

    private List<ChuqiyouFk_riskData_emergency_contacts> emergency_contacts;

    private ChuqiyouFk_riskData_present present;

    private JSONArray contact;
    

}