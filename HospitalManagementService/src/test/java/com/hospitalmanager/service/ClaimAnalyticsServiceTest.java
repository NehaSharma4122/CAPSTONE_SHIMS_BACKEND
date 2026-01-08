package com.hospitalmanager.service;

import com.hospitalmanager.feign.ClaimFeignClient;
import com.hospitalmanager.request.ClaimStatsDTO;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaimAnalyticsServiceTest {

    @Mock
    private ClaimFeignClient feignClient;

    @InjectMocks
    private ClaimAnalyticsService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enriches_totals_correctly() {

        ClaimStatsDTO dto = new ClaimStatsDTO();
        dto.setOperationCost(100.0);
        dto.setMedicineCost(50.0);
        dto.setPostOpsCost(null);

        when(feignClient.getClaimsByHospital(5L))
                .thenReturn(List.of(dto));

        List<ClaimStatsDTO> result =
                service.getHospitalClaimsWithTotals(5L);

        assertEquals(BigDecimal.valueOf(150.0),
                result.get(0).getClaimAmount());

        assertEquals(
                result.get(0).getClaimAmount(),
                result.get(0).getApprovedAmount()
        );

        verify(feignClient).getClaimsByHospital(5L);
    }
}
