package com.tt.service;

import com.tt.common.PhysicalServiceImpl;
import com.tt.mapper.FeeConfigMapper;
import com.tt.po.FeeConfig;
import org.springframework.stereotype.Service;

@Service
public class FeeConfigService extends PhysicalServiceImpl<FeeConfigMapper, FeeConfig> {
}
