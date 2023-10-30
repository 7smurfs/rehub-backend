package sevensmurfs.rehub.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sevensmurfs.rehub.model.entity.Faq;
import sevensmurfs.rehub.model.message.request.FaqRequest;
import sevensmurfs.rehub.model.message.response.FaqResponse;
import sevensmurfs.rehub.service.FaqService;

import java.util.List;

@RestController
@RequestMapping("/v1/faq")
@RequiredArgsConstructor
@Slf4j
public class FaqController {

    private final FaqService faqService;

    /**
     * Add a new faq Admin POST > /api/v1/faq
     */
    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> addFaq(@Validated @RequestBody FaqRequest faqRequest) throws Exception {

        log.info(" > > > POST /api/v1/faq (Adding a new faq)");

        Faq faq = faqService.addFaq(faqRequest);

        log.info(" < < < POST /api/v1/faq (Adding a new faq successful)");

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .path("/{faqId}")
                                                                 .buildAndExpand(faq.getId())
                                                                 .toUri())
                             .build();
    }

    /**
     * Get all faq request GET > /api/v1/faq
     */
    @GetMapping
    public ResponseEntity<Object> getAllFaq() {

        log.info(" > > > GET /api/v1/faq (Get all faq request)");

        List<Faq> faqList = faqService.getAllFaq();

        log.info(" < < < GET /api/v1/faq (Get all faq success)");

        return ResponseEntity.ok(faqList.stream().map(FaqResponse::mapFaqEntity).toList());
    }

    /**
     * Delete faq ADMIN request DELETE > /api/v1/faq/:id
     */
    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Object> deleteFaq(@PathVariable(name = "id") Long id) {

        log.info(" > > > DELETE /api/v1/faq/{} (Delete faq request)", id);

        faqService.deleteFaqWithId(id);

        log.info(" < < < DELETE /api/v1/faq/{} (Delete faq success)", id);

        return ResponseEntity.noContent().build();
    }
}
