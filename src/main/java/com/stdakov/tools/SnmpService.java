package com.stdakov.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class SnmpService {
    private Snmp snmp = null;
    private int retries = 2;
    private int timeout = 2000;
    private String address = null;
    private boolean debug = false;

    private static final Logger logger = LoggerFactory.getLogger(SnmpService.class);

    public SnmpService(String hostname) {
        this(hostname, 161);
    }

    public SnmpService(String hostname, int port) {
        this.initAddress(hostname, port);
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void initAddress(String hostname, int port) {
        this.address = hostname + "/" + port;
    }

    public void open() throws IOException {
        if (this.debug) {
            logger.info("Initiating SNMP session");
        }
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public void close() {
        if (this.debug) {
            logger.info("Stopping the SNMP session");
        }
        if (snmp != null) {
            try {
                snmp.close();
            } catch (IOException e) {
                logger.error("Exception when trying to close SNMP session: " + e.getMessage());
            }
        }
    }

    public Optional<String> get(String community, int version, String oid) {
        Target target = this.createTarget(community, version, this.retries, this.timeout);
        Optional<ResponseEvent> responseEvent = this.request(PDU.GET, oid, target);

        String value = null;

        if (responseEvent.isPresent() && responseEvent.get().getResponse().size() > 0) {
            value = responseEvent.get().getResponse().get(0).getVariable().toString();
        }

        return Optional.ofNullable(value);
    }

    public boolean set(String community, int version, String oid) {
        Target target = this.createTarget(community, version, this.retries, this.timeout);
        return this.request(PDU.SET, oid, target).isPresent();
    }

    public Target createTarget(String community, int version, int retries, int timeout) {

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setVersion(version);
        target.setAddress(new UdpAddress(this.address));
        target.setRetries(retries);
        target.setTimeout(timeout);

        return target;
    }

    public Optional<ResponseEvent> request(int requestType, String oid, Target target) {
        ResponseEvent response = null;
        try {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            pdu.setType(requestType);
            pdu.setRequestID(new Integer32(1));

            response = this.snmp.send(pdu, target);
        } catch (IOException e) {
            logger.error("SNMP Error: " + e.getMessage());
        }

        return Optional.ofNullable(response);
    }

    public Map<String, String> walk(String oid, String community, int version) {

        Target target = this.createTarget(community, version, this.retries, this.timeout);

        return this.walk(oid, target);
    }

    public Map<String, String> walk(String oid, Target target) {

        TreeUtils treeUtils = new TreeUtils(this.snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, new OID(oid));

        Map<String, String> result = new TreeMap<>();
        for (TreeEvent event : events) {
            if (event == null) {
                continue;
            }
            if (event.isError()) {
                logger.error("Error: table OID [" + oid + "] " + event.getErrorMessage());
                continue;
            }
            VariableBinding[] varBindings = event.getVariableBindings();
            if (varBindings == null) {
                continue;
            }

            for (VariableBinding varBinding : varBindings) {
                if (varBinding == null) {
                    continue;
                }
                result.put(varBinding.getOid().toString(), varBinding.getVariable().toString());
            }
        }

        return result;
    }
}
