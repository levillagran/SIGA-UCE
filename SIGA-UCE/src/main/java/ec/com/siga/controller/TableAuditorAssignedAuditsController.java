package ec.com.siga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.log.SysoCounter;

import ec.com.siga.entity.CheckList;
import ec.com.siga.entity.Informe;
import ec.com.siga.service.AuditService;
import ec.com.siga.service.AuditorService;
import ec.com.siga.service.BackOfficeService;
import ec.com.siga.service.CustService;
import ec.com.siga.service.InformeService;

@Controller
public class TableAuditorAssignedAuditsController {

	@Autowired
	@Qualifier("informeServicio")
	private InformeService informeServicio;

	@Autowired
	@Qualifier("auditService")
	private AuditService auditService;

	@Autowired
	@Qualifier("auditorService")
	private AuditorService auditorService;

	@Autowired
	@Qualifier("backOfficeService")
	private BackOfficeService backOfficeService;

	@Autowired
	@Qualifier("custService")
	private CustService custService;

	@GetMapping("/tableAssignedAudits")
	@ResponseBody
	public ModelAndView showForm(String usuario) {
		ModelAndView mav = new ModelAndView("tableAssignedAudits");
		mav.addObject("contacts", auditorService.findAllAssignedAudits(usuario));
		mav.addObject("usuario", usuario);
		return mav;
	}

	@GetMapping("/tableAuditsSendNC")
	@ResponseBody
	public ModelAndView showFormNoNC(String usuario) {
		ModelAndView mav = new ModelAndView("tableAuditsSendNC");
		mav.addObject("contacts", auditorService.findAllAuditsSendNC(usuario));
		mav.addObject("usuario", usuario);
		return mav;
	}

	@GetMapping("/tableAssignedAuditsCheck")
	@ResponseBody
	public ModelAndView showFormCheck(String usuario) {
		ModelAndView mav = new ModelAndView("tableAuditsToCheck");
		mav.addObject("contacts", auditorService.findAllAuditsToCheck(usuario));
		mav.addObject("usuario", usuario);
		return mav;
	}

	@GetMapping("/tableAssignedAuditsH")
	@ResponseBody
	public ModelAndView showFormH(String usuario) {
		ModelAndView mav = new ModelAndView("tableAuditAuditsHistory");
		mav.addObject("contacts", auditorService.findAllAuditsHistory(usuario));
		mav.addObject("usuario", usuario);
		return mav;
	}

	@GetMapping("/startQuestionnaire")
	@ResponseBody
	public ModelAndView startquestionnaire1(int id, String usuario) {
		ModelAndView mav = new ModelAndView("questionnaireForm");
		auditorService.createCkeckList(id); // crea las preguntas del cuestionario solicitado con el id de la solicitud
		CheckList cl = auditorService.reply(id);// consulta la primera pregunta del cuestionario

		String tpreg="";
		switch (cl.getSolicitudAuditoriaId().getTipoAuditoriaId().getTipoAuditoriaId()) {
		case 1:
			tpreg = "30";
			break;
		case 2:
			tpreg = "37";
			break;
		case 3:
			tpreg = "36";
			break;
		case 4:
			tpreg = "35";
			break;
		default:
			break;
		}

		mav.addObject("pregunta", cl);
		mav.addObject("id", id);
		mav.addObject("usuario", usuario);
		mav.addObject("codigoString", String.valueOf(cl.getCodigo()));

		String numPre = String.valueOf(cl.getCodigo());
		int indexString = numPre.length();
		String numPreAux = (numPre.substring(indexString - 2)) + "/" + tpreg;
		mav.addObject("numPre", numPreAux);

		return mav;
	}

	@GetMapping("/startCheckFiles")
	@ResponseBody
	public ModelAndView startquestionnaireCheck(int id, String usuario) {
		ModelAndView mav = new ModelAndView("questionnaireCheckFile");
		CheckList cl = auditorService.replyUploadFile(id);
		mav.addObject("pregunta", cl);
		mav.addObject("id", id);
		mav.addObject("usuario", usuario);
		mav.addObject("codigoString", String.valueOf(cl.getCodigo()));
		return mav;
	}

	@PostMapping("/QuestionSave")
	public ModelAndView nextQuestionPost(int id, String usuario, String codigo, MultipartFile foto, String evidencia,
			boolean respuesta) {
		ModelAndView mav = new ModelAndView("save");
		System.out.println("entra en quest save");

		System.out.println(codigo);
		System.out.println(foto);
		System.out.println(evidencia);
		System.out.println(respuesta);
		auditorService.saveReply(foto, evidencia, respuesta, codigo);
		return mav;
	}

	@PostMapping("/nextQuestion")
	public ModelAndView nextQuest(int id, String usuario, String codigo) {
		ModelAndView mav = new ModelAndView("questionnaireForm");
		String accion = "+";
		CheckList cl = auditorService.replyPost(id, codigo, accion);
		mav.addObject("pregunta", cl);
		mav.addObject("id", id);
		mav.addObject("username", usuario);
		mav.addObject("codigoString", String.valueOf(cl.getCodigo()));
		
		String tpreg="";
		switch (cl.getSolicitudAuditoriaId().getTipoAuditoriaId().getTipoAuditoriaId()) {
		case 1:
			tpreg = "30";
			break;
		case 2:
			tpreg = "37";
			break;
		case 3:
			tpreg = "36";
			break;
		case 4:
			tpreg = "35";
			break;
		default:
			break;
		}

		String numPre = String.valueOf(cl.getCodigo());
		int indexString = numPre.length();
		String numPreAux = (numPre.substring(indexString - 2)) + "/" + tpreg;
		mav.addObject("numPre", numPreAux);

		return mav;
	}

	@PostMapping("/previousQuestion")
	public ModelAndView preQuest(int id, String usuario, String codigo) {
		ModelAndView mav = new ModelAndView("questionnaireForm");
		String accion = "-";
		CheckList cl = auditorService.replyPost(id, codigo, accion);
		mav.addObject("pregunta", cl);
		mav.addObject("id", id);
		mav.addObject("username", usuario);
		mav.addObject("codigoString", String.valueOf(cl.getCodigo()));
		
		String tpreg="";
		switch (cl.getSolicitudAuditoriaId().getTipoAuditoriaId().getTipoAuditoriaId()) {
		case 1:
			tpreg = "30";
			break;
		case 2:
			tpreg = "37";
			break;
		case 3:
			tpreg = "36";
			break;
		case 4:
			tpreg = "35";
			break;
		default:
			break;
		}

		String numPre = String.valueOf(cl.getCodigo());
		int indexString = numPre.length();
		String numPreAux = (numPre.substring(indexString - 2)) + "/" + tpreg;
		mav.addObject("numPre", numPreAux);

		return mav;
	}

	@PostMapping("/nextQuestionCheckFile")
	public ModelAndView nextQuestCheck(int id, String usuario, String codigo) {
		ModelAndView mav = new ModelAndView("questionnaireCheckFile");
		String accion = "+";
		CheckList cl = auditorService.replyPostUploadFile(id, codigo, accion);
		mav.addObject("pregunta", cl);
		mav.addObject("id", id);
		mav.addObject("username", usuario);
		mav.addObject("codigoString", String.valueOf(cl.getCodigo()));
		return mav;
	}

	@PostMapping("/previousQuestionCheckFile")
	public ModelAndView preQuestCheck(int id, String usuario, String codigo) {
		ModelAndView mav = new ModelAndView("questionnaireCheckFile");
		String accion = "-";
		CheckList cl = auditorService.replyPostUploadFile(id, codigo, accion);
		mav.addObject("pregunta", cl);
		mav.addObject("id", id);
		mav.addObject("username", usuario);
		mav.addObject("codigoString", String.valueOf(cl.getCodigo()));
		return mav;
	}

	@GetMapping("/sendNC")
	@ResponseBody
	public String sendNonConformities(Integer id, String usuario) {
		String msg = auditorService.sendNonConformities(id);
		return msg;
	}

	@GetMapping("/editAssignedAudits")
	@ResponseBody
	public ModelAndView showEditAdminForm(Integer id, String usuario) {
		ModelAndView mav = new ModelAndView("editAuditsRequests");
		mav.addObject("reporte", informeServicio.findReport(id));
		mav.addObject("auditores", backOfficeService.findAllAudit());
		mav.addObject("usuario", usuario);
		return mav;
	}

	@PostMapping("/saveAssignedAudits")
	public ModelAndView saveAdmin(int informeId, String usuario, String auditorId) throws Exception {
		ModelAndView mav = new ModelAndView("/dashboardBack");
		// backOfficeService.saveInforme(informeId, auditorId, sa);
		mav.addObject("username", usuario);
		return mav;
	}

	@GetMapping("/findAssignedAudits")
	@ResponseBody
	public Informe findOne(Integer id) {
		return informeServicio.findReport(id);
	}

	@GetMapping("/cancelAssignedAudits")
	public String cancel() {
		return "redirect:/dashboardAdmin";
	}

	@GetMapping("/deleteAssignedAudits")
	public void deleteCountry(Integer adminId) {
		informeServicio.deleteReport(adminId);

	}

	@GetMapping("/viewCust")
	@ResponseBody
	public ModelAndView viewCust(int id) {
		ModelAndView mav = new ModelAndView("viewCustomer");
		mav.addObject("cliente", custService.findCustById(id));
		return mav;
	}

}
