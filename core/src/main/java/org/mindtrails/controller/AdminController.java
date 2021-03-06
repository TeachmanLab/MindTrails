package org.mindtrails.controller;

import lombok.Data;
import org.joda.time.DateTime;
import org.mindtrails.domain.*;
import org.mindtrails.domain.Scheduled.Email;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Scheduled.ScheduledEventComparator;
import org.mindtrails.domain.forms.ParticipantCreate;
import org.mindtrails.domain.forms.ParticipantCreateAdmin;
import org.mindtrails.domain.forms.ParticipantUpdateAdmin;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.tango.Account;
import org.mindtrails.domain.tango.Item;
import org.mindtrails.domain.tango.OrderResponse;
import org.mindtrails.domain.tracking.ErrorLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.persistence.*;
import org.mindtrails.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private static final int PER_PAGE=20; // Number of users to display per page.

    @Autowired
    private EmailService emailService;

    @Autowired
    private ScheduledEventService scheduledEventService;

    @Autowired
    private TangoService tangoService;

    @Autowired
    private ExportService exportService;


    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private ErrorLogRepository SMSLogRepository;

    @Autowired
    private EmailLogRepository emailLogRepository;

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private ActionLogRepository actionSequenceRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private JsPsychRepository jsPsychRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private GiftLogRepository giftLogRepository;

    @Value("${import.url}")
    private String url;

    @Autowired
    private ImportService importService;

    @Override
    @ModelAttribute("visiting")
    public boolean visiting(Principal principal) {
        return true;
    }


    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Data
    class csData {

        String condition;
        String session;
        int active;
        int inactive;
        int total;
        csData(String condition,String session, int active,int inactive, int total) {
            this.condition = condition;
            this.session = session;
            this.active=active;
            this.inactive=inactive;
            this.total=total;

        }
    }
    @Data
    class csDataMap{
        String session;
        Map<String,Integer> csMap=new HashMap<String,Integer>();

        csDataMap(String session,Map<String,Integer> csMap){
            this.session=session;
            this.csMap=csMap;
        }
    }



    @Data
    class DateData {
        int count = 0;
        Date date = new Date();
        DateData(Date date, int count) {this.count = count; this.date = date;}
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listParticipants(ModelMap model,Principal principal,
                            final @RequestParam(value = "search", required = false, defaultValue = "") String search,
                            final @RequestParam(value = "page", required = false, defaultValue = "0") String pageParam) {

        Page<Participant> daoList;
        PageRequest pageRequest;

        int page = Integer.parseInt(pageParam);
        pageRequest = new PageRequest(page, PER_PAGE);
        if(search.isEmpty()) {
            daoList = participantRepository.findAll(pageRequest);
        } else {
            daoList = participantRepository.search(search, pageRequest);
        }
        model.addAttribute("search", search);
        model.addAttribute("paging", daoList);
        model.addAttribute("participants", daoList);
        return "admin/participants";
    }

    @RequestMapping(value="errors", method=RequestMethod.GET)
    public String listErrors(ModelMap model,Principal principal,
                             final @RequestParam(value = "page", required = false, defaultValue = "0") String pageParam) {

        Page<ErrorLog> logs;
        PageRequest pageRequest;

        int page = Integer.parseInt(pageParam);
        pageRequest = new PageRequest(page, PER_PAGE,
                Sort.Direction.DESC, "dateSent");
        logs = errorLogRepository.findAll(pageRequest);
        model.addAttribute("errorLogs", logs);
        return "admin/listErrors";

    }


    @RequestMapping(value="/participant/{id}", method=RequestMethod.GET)
    public String participantUpdateForm(ModelMap model,
                           @PathVariable("id") long id) {
        Participant p;
        ParticipantUpdateAdmin form;
        p    = participantRepository.findOne(id);
        form = new ParticipantUpdateAdmin(p);
        model.addAttribute("participantUpdateAdmin", form);
        model.addAttribute("participantEdit", p);
        model.addAttribute("coaches", participantRepository.findCoaches());
        return "admin/participantUpdate";
    }

    @ExportMode
    @RequestMapping(value="/participant/{id}", method=RequestMethod.POST)
    public String updateParticipant(ModelMap model,
                                       @PathVariable("id") long id,
                                       @Valid ParticipantUpdateAdmin form,
                                       BindingResult bindingResult) {
        Participant p;
        p = participantRepository.findOne(id);
        if(bindingResult.hasErrors()) {
            model.addAttribute("coaches", participantRepository.findCoaches());
            model.addAttribute("participantAdminForm", form);
            model.addAttribute("participantEdit", p);
            return "admin/participantUpdate";
        } else {
            form.updateParticipant(p);
            participantService.save(p);
            return "redirect:/admin";
        }
    }

    @RequestMapping(value="/participant/", method=RequestMethod.GET)
    public String participantCreateForm(ModelMap model) {

        ParticipantCreate form = new ParticipantCreate();

        model.addAttribute("participantCreateAdmin", form);
        model.addAttribute("coaches", participantRepository.findCoaches());

        return "admin/participantCreate";
    }

    @RequestMapping(value="/participant/", method=RequestMethod.POST)
    public String createParticipant(ModelMap model,
                                    @Valid ParticipantCreateAdmin participantCreateAdmin,
                                    BindingResult bindingResult,
                                    HttpSession session) {
        Participant participant;

        if(!participantCreateAdmin.validParticipant(bindingResult, participantService)) {
            model.addAttribute("coaches", participantRepository.findCoaches());
            model.addAttribute("participantCreateAdmin", participantCreateAdmin);
            return "admin/participantCreate";
        }

        participant = participantService.create();
        participant = participantCreateAdmin.updateParticipant(participant);
        participant.setReference("Admin");
        participantService.save(participant);
        LOG.info("Participant created.");
        return "redirect:/admin";
    }


    @RequestMapping(value="/listEvents", method=RequestMethod.GET)
    public String listEvents(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);

        // group emails by type.
        List<ScheduledEvent> events = scheduledEventService.getScheduledEvents();
        Collections.sort(events, new ScheduledEventComparator(p.getStudy()));
        model.addAttribute("events", events);
        return "admin/listEvents";
    }

    @ExportMode
    @RequestMapping(value="/sendEmail/{type}")
    public String sendEmail(ModelMap model, Principal principal,
                            @PathVariable("type") String type) throws Exception {
        Participant p = participantService.get(principal);

        if(type.equals("giftCard")) {
            Item item = tangoService.getCatalog().findItemByCountryCode("US");
            GiftLog log = new GiftLog(p, "test", 1,1, item);
            this.giftLogRepository.save(log);
            OrderResponse reward = tangoService.awardGiftCard(log);  // This would actually award a gift card, if you need to do some testing.
            this.emailService.sendGiftCard(p, reward, log);
        } else if(type.equals("resetPass")) {
            p.setPasswordToken(new PasswordToken());
            this.emailService.sendPasswordReset(p);
        } else if(type.equals("alertAdmin")) {
            this.emailService.sendAdminEmail("Example", "This is an example alert message normally sent to the administrator with a custom subject and message");
        } else {
            Email email = emailService.getEmailForType(type);
            email.setParticipant(p);
            email.setTo(p.getEmail());
            email.setContext(new Context());
            emailService.sendExample(email);
        }
        return "redirect:/admin/listEvents";
    }

    @RequestMapping(value="/listSessions", method=RequestMethod.GET)
    public String listSessions(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("sessions", p.getStudy().getSessions());
        return "admin/listSessions";
    }

    // Trying to write a methods to get Tango Account information. By Diheng
    @RequestMapping(value="/tango",method = RequestMethod.GET)
    public String tangoInfo(ModelMap model, Principal principal,
                            final @RequestParam(value = "page", required = false, defaultValue = "0") String pageParam){

        Page<GiftLog> giftPages;
        PageRequest pageRequest;
        int page = Integer.parseInt(pageParam);
        pageRequest = new PageRequest(page, PER_PAGE);

        Account a = tangoService.getAccountInfo();

        long numberAwarded = 0, amountAwarded = 0;
        try {
            numberAwarded = giftLogRepository.countGiftLogByOrderIdIsNotNull();
            amountAwarded = giftLogRepository.totalAmountAwarded();
        } catch (NullPointerException npe) {
            // Noop.  These counts might come back as null from JPA Respoitory, so don't fail when that happens/
        }

        model.addAttribute("account",a);
        giftPages = giftLogRepository.awardableGiftLogs(pageRequest);
        model.addAttribute("giftLogs", giftPages);
        model.addAttribute("numberAwarded",numberAwarded);
        model.addAttribute("amountAwarded", amountAwarded);
        return "admin/tango";
    }

    // Trying to write a methods to get Tango Account information. By Diheng
    @RequestMapping(value="/tangoHistory",method = RequestMethod.GET)
    public String tangoHistory(ModelMap model, Principal principal,
                            final @RequestParam(value = "page", required = false, defaultValue = "0") String pageParam){

        Page<GiftLog> giftPages;
        PageRequest pageRequest;
        int page = Integer.parseInt(pageParam);
        pageRequest = new PageRequest(page, PER_PAGE);
        giftPages = giftLogRepository.findAll(pageRequest);
        model.addAttribute("giftLogs", giftPages);
        return "admin/tangoHistory";
    }


    // Trying to write a methods to get Tango Account information. By Diheng
    @RequestMapping(value="/tango",method = RequestMethod.POST)
    public String awardGiftCards(ModelMap model, Principal principal, @RequestParam(value="id") Long[] ids) {

        for(long id : ids) {
            GiftLog log = giftLogRepository.findOne(id);
            OrderResponse reward = this.tangoService.awardGiftCard(log);
            this.emailService.sendGiftCard(log.getParticipant(), reward, log);
        }
        return tangoInfo(model, principal, "0");
    }


    @RequestMapping(value="/participant/{id}/awardCard", method=RequestMethod.POST)
    public String giftCard(ModelMap model, @PathVariable("id") long id) throws Exception {

        Participant p = participantRepository.findOne(id);
        GiftLog log = tangoService.createGiftLogUnsafe(p, "AdminAwarded", 5);
        OrderResponse reward = tangoService.awardGiftCard(log);
        this.emailService.sendGiftCard(p, reward, log);
        return "redirect:/admin/participant/" + id;
    }

    @RequestMapping(value="/rewardInfo/{orderId}", method = RequestMethod.GET)
    public String showRewardInfo(ModelMap model, Principal principal, @PathVariable ("orderId") String orderId) {
        OrderResponse order = tangoService.getOrderInfo(orderId);
        model.addAttribute("order",order);
        return "admin/rewardInfo";
    }

    @RequestMapping(value="/participant/{id}/resendGift/{giftLogId}", method=RequestMethod.GET)
    public String resendGiftCard(@PathVariable("id") long id,
                                 @PathVariable("giftLogId") long giftLogId) throws Exception {
        GiftLog log = giftLogRepository.findOne(giftLogId);
        Participant p = participantRepository.findOne(id);
        OrderResponse reward = tangoService.getOrderInfo(log.getOrderId());
        this.emailService.sendGiftCard(p, reward, log);
        return "redirect:/admin/participant/" + id;
    }


    @RequestMapping(value="/export", method=RequestMethod.GET)
    public String export(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        List<Scale> scales;

        if (importService.isImporting()) {
            try {
                List<Scale> remoteScales = importService.fetchListOfScales();
                List<Scale> localScales = exportService.listRepositories();
                for(Scale local : localScales) {
                    for (Scale remote : remoteScales) {
                        if(local.getName().equals(remote.getName())) {
                            local.setRemoteSize(remote.getSize());
                        }
                    }
                }
                scales = localScales;
            } catch (Exception e) {
                // may not be able to contact the importService, don't just die here.
                model.addAttribute("scales_error", "unable to load scales from import service.");
                scales = new ArrayList<>();
            }
        } else {
            scales = exportService.listRepositories();
        }

        Collections.sort(scales, Comparator.comparing(Scale::getName));

        model.addAttribute("scales", scales);
        model.addAttribute("importMode",importService.isImporting());
        model.addAttribute("exportMaxMinutes", exportService.getMaxMinutes());
        model.addAttribute("exportMaxRecords", exportService.getMaxRecords());
        model.addAttribute("totalRecords", exportService.totalDeleteableRecords());
        model.addAttribute("minutesSinceLastExport", exportService.minutesSinceLastExport());
        model.addAttribute("formsDisabled", exportService.disableAdditionalFormSubmissions());

        model.addAttribute("participant", p);
        model.addAttribute("sessions", p.getStudy().getSessions());
        model.addAttribute("hideAccountBar", true);
        return "admin/export";
    }

    @RequestMapping(value="/export/clear" , method= RequestMethod.GET)
    public String clearOldData(ModelMap model, Principal principal) {
        importService.clearOldData();
        return (this.export(model,principal));
    }


    @RequestMapping(value="/userstats", method= RequestMethod.GET)
    public String sayAloha(@RequestParam(value="one", required=false, defaultValue="") String name, ModelMap model,Principal principal){

        Study currentstudy = getParticipant(principal).getStudy();

        List<ParticipantStats> daoList;
        List<StudyStats> studyList;


        Date lastWeek = DateTime.now().minusDays(7).toDate();
        Long errorLogs = errorLogRepository.countByDateSentAfter(lastWeek);
        Long emailLogs = emailLogRepository.countByDateSentAfter(lastWeek);
        Long SMSLogs = SMSLogRepository.countByDateSentAfter(lastWeek);

        Long taskLogs = taskLogRepository.countByDateCompletedAfter(lastWeek);
        Long actionLogs = actionSequenceRepository.countByDateAfter(lastWeek);

        daoList=participantRepository.findAllStatsBy();
        studyList=studyRepository.findAllStatsBy();

        Long lastLoginNum=participantRepository.countByLastLoginDateAfter(lastWeek);

        userstats users= new userstats(daoList,studyList,currentstudy);

        Map<userstats.Key, Integer> csActiveMap = users.getConditionSessionActiveMap();
        Map<userstats.Key, Integer> csInActiveMap = users.getConditionSessionInActiveMap();
        Map<userstats.Key, Integer> csMap = users.getConditionSessionMap();
        List<Session> sessionList = users.getSessionList();
        List<String> conditionList = users.getConditionList();

        ArrayList csData = new ArrayList<csData>();

        for (Session session : sessionList) {
            for (String condition : conditionList) {
                userstats.Key csKey = new userstats.Key(session.getName(), condition);
                csData.add(new csData(condition, session.getName(), csActiveMap.get(csKey), csInActiveMap.get(csKey), csMap.get(csKey)));
            }

        }
        model.addAttribute("errorLogs", errorLogs);
        model.addAttribute("emailLogs", emailLogs);
        model.addAttribute("SMSLogs", SMSLogs);
        model.addAttribute("participants", daoList);
        model.addAttribute("taskLogs", taskLogs);
        model.addAttribute("actionLogs", actionLogs);
        model.addAttribute("lastLoginNum",lastLoginNum);
        model.addAttribute("users",users);
        model.addAttribute("csData",csData);
        model.addAttribute("conditionList", conditionList);
        
        return "admin/userstats";

    }


    @RequestMapping(value="/getLoginCounts", method= RequestMethod.GET)
    public @ResponseBody List<DateData> myData(Principal principal) {
        Study currentstudy = getParticipant(principal).getStudy();

        List<ParticipantStats> daoList;
        List<StudyStats> studyList;

        daoList=participantRepository.findAllStatsBy();
        studyList=studyRepository.findAllStatsBy();
        userstats users= new userstats(daoList,studyList,currentstudy);
        Map<Date,Integer> loginMap=users.getLoginMap();

        ArrayList dateData = new ArrayList<DateData>();


        for (Map.Entry<Date,Integer> entry : loginMap.entrySet()) {
            dateData.add(new DateData(entry.getKey(),entry.getValue()));
        }
        return dateData;
    }


    @RequestMapping(value="/getFlow",method = RequestMethod.GET)
    public @ResponseBody Map<String,ArrayList<countMap>> consortDiagram(Principal principal) {
        Study currentStudy = getParticipant(principal).getStudy();
        FlowCount f = new FlowCount(currentStudy, studyRepository, visitRepository,participantRepository,jsPsychRepository,taskLogRepository);
        ArrayList<countMap> countPairs = f.getPairs();
        Map<String,ArrayList<countMap>> flowCountMap = new HashMap<>();
        flowCountMap.put("Total", countPairs);
        return flowCountMap;
    }


    @RequestMapping(value = "/getScale", method = RequestMethod.GET)
    public @ResponseBody Map<String, ArrayList<countMap>> completeReport(Principal principal) {
        Study currentStudy = getParticipant(principal).getStudy();
        List<Scale> scaleList = importService.fetchListOfScales();
        List<Participant> realAccount = participantRepository.findParticipantsByTestAccountIsFalseAndAdminIsFalse();
        Map<String,ArrayList<countMap>> scaleCountMap = new HashMap<>();
        for (Scale scale:scaleList) {
            JpaRepository rep = exportService.getRepositoryForName(scale.getName(), false);
            ScaleCount s = new ScaleCount(currentStudy,scale,realAccount,rep);
            if (!(s.getUniqueIDCount().equals(Long.valueOf(-1)))) {scaleCountMap.put(s.getScaleName(),s.getPairs());}
        }
        return scaleCountMap;


    }


    @RequestMapping(value="/getCondition", method= RequestMethod.GET)
    public @ResponseBody Map<String,ArrayList<csDataMap>> dashboard(Principal principal) {
        Study currentstudy = getParticipant(principal).getStudy();

        List<ParticipantStats> daoList;
        List<StudyStats> studyList;
        //Find all participants
        daoList = participantRepository.findAllStatsBy();
        //Find all study information
        studyList = studyRepository.findAllStatsBy();
        userstats users = new userstats(daoList, studyList, currentstudy);
        Map<userstats.Key, Integer> csActiveMap = users.getConditionSessionActiveMap();
        Map<userstats.Key, Integer> csInActiveMap = users.getConditionSessionInActiveMap();
        Map<userstats.Key, Integer> csMap = users.getConditionSessionMap();
        List<Session> sessionList = users.getSessionList();
        List<String> conditionList = users.getConditionList();

        ArrayList csDataMapList1 = new ArrayList<csDataMap>();
        ArrayList csDataMapList2 = new ArrayList<csDataMap>();
        ArrayList csDataMapList3 = new ArrayList<csDataMap>();

        Map<String,ArrayList<csDataMap>> massiveMap=new HashMap<String,ArrayList<csDataMap>>();

        for (Session session : sessionList) {
            Map<String,Integer> temMap1= new HashMap<String,Integer>();
            Map<String,Integer> temMap2= new HashMap<String,Integer>();
            Map<String,Integer> temMap3= new HashMap<String,Integer>();
            for (String condition : conditionList) {
                userstats.Key csKey = new userstats.Key(session.getName(), condition);
                temMap1.put(condition,csMap.get(csKey));
                temMap2.put(condition,csActiveMap.get(csKey));
                temMap3.put(condition,csInActiveMap.get(csKey));

            }
            csDataMapList1.add(new csDataMap(session.getName(),temMap1));
            csDataMapList2.add(new csDataMap(session.getName(),temMap2));
            csDataMapList3.add(new csDataMap(session.getName(),temMap3));

        }
        massiveMap.put("total",csDataMapList1);
        massiveMap.put("active",csDataMapList2);
        massiveMap.put("inactive",csDataMapList3);

        return massiveMap;
    }


}
