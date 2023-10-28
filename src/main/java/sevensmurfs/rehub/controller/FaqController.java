package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sevensmurfs.rehub.model.entity.Faq;
import sevensmurfs.rehub.model.message.response.EmployeeResponse;
import sevensmurfs.rehub.model.message.response.FaqResponse;
import sevensmurfs.rehub.service.FaqService;

import java.util.List;

@RestController
@RequestMapping("/v1/faq")
@RequiredArgsConstructor
@Slf4j
public class FaqController {

    public final FaqService faqService;


    /**
     * Add a new faq Admin POST > /api/v1/faq
     */

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> addFaq(@RequestBody Faq newFaq) throws Exception {

        log.info(" > > > POST /api/v1/faq (Adding a new faq)");

        Faq faq = faqService.addFaq(newFaq);

        log.info(" > > > POST /api/v1/faq (Adding a new faq succesful)");

        return ResponseEntity.ok(faq);
    }


    /**
     * Get all faq request GET > /api/v1/faq
     */

    @GetMapping
    public ResponseEntity<Object> getAllFaq(){

        log.info(" > > > GET /api/v1/faq (Get all faq ADMIN request)");

        List<Faq> faqList = faqService.getAllFaq();

        log.info(" < < < GET /api/v1/faq (Get all faq ADMIN success)");

        return ResponseEntity.ok(faqList.stream().map(FaqResponse::mapFaqEntity).toList());
    }
}
