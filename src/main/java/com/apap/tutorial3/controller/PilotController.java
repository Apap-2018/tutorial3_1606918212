package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

@Controller
public class PilotController {
	
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id", required = true) String id,
					@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
					@RequestParam(value = "name", required = true) String name,
					@RequestParam(value = "flyHour", required = true) int flyHour) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}
	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		model.addAttribute("pilot", archive);
		return "view-pilot";
	}
	
	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		
		model.addAttribute("listPilot", archive);
		return "viewall-pilot";
	}
	
	@RequestMapping(value = {"/pilot/view/license-number" , "/pilot/view/license-number/{licenseNumber}"})
	public String viewPathVariable(@PathVariable Optional<String> licenseNumber, Model model) {
		String existence = "viewPathVariable";
		List<PilotModel> archive = pilotService.getPilotList();
		
		
		if(licenseNumber.isPresent()) {
			PilotModel archives = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			for (PilotModel p : archive) {
				if(p.getLicenseNumber().equals(licenseNumber.get())) {
					archives = p;
					model.addAttribute("pilot", archives);
					existence = "view-pilot";
				}
			}
		}
		
		return existence;	
	}
	
	@RequestMapping(value = {"pilot/update/license-number/{licenseNumber}/fly-hour/{newFlyHour}", 
			"pilot/update/license-number/fly-hour/{newFlyHour}",
			"pilot/update/license-number/{licenseNumber}/fly-hour"})
	public String update(@PathVariable("licenseNumber") Optional<String> licenseNumber, 
			@PathVariable ("newFlyHour") Optional<String> newFlyHour, 
			Model model) {
		String exist = "updateError";
		List<PilotModel> archive = pilotService.getPilotList();
		
		if(licenseNumber.isPresent() && (newFlyHour.isPresent())){
			PilotModel archives = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			for (PilotModel p : archive) {
				if(p.getLicenseNumber().equals(licenseNumber.get())) {
					p.setFlyHour(Integer.parseInt(newFlyHour.get()));
					archives = p;
					
					model.addAttribute("pilot", archives);
					exist = "update";
				}
			}
		}
		return exist;
	}
	
	@RequestMapping("/pilot/delete/id/{id}")
	public String delete(@PathVariable Optional<String> id, Model model) {
		String existence = "deleteError";
		List<PilotModel> archive = pilotService.getPilotList();
		
		if(id.isPresent()) {
			for (PilotModel p : archive) {
				if(p.getId().equalsIgnoreCase(id.get())) {
					pilotService.removePilot(p);
					
					model.addAttribute("id", id.get());
					return "delete";
				}
					
				}
			}
		return existence;
	}
		

}
