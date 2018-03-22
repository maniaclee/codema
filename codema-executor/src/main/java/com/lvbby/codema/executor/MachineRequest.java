package com.lvbby.codema.executor;

import com.lvbby.codema.core.bean.BaseEntity;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: MachineRequest.java, v 0.1 2018年03月21日 下午7:20 dushang.lp Exp $
 */
public class MachineRequest extends BaseEntity {

    private static final long serialVersionUID = -4143408148595085610L;

    private String machineId;
    private List<MachineRequestArg> args;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public List<MachineRequestArg> getArgs() {
        return args;
    }

    public void setArgs(List<MachineRequestArg> args) {
        this.args = args;
    }
}