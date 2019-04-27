package ec.com.siga.service;


import java.util.List;

import ec.com.siga.entity.CheckList;
import ec.com.siga.entity.Informe;

public interface AuditorService {
	public abstract List<Informe> findAllAssignedAudits(String auditor);
	public abstract void createCkeckList(int informeId);
	public abstract CheckList reply(int informeId);
	public abstract CheckList replyPost(int informeId, String codigo, String accion);
}
