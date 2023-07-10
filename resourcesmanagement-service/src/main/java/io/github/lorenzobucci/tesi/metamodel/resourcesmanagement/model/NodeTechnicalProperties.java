package io.github.lorenzobucci.tesi.metamodel.resourcesmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class NodeTechnicalProperties {

    @Column(name = "memory_MB", nullable = false)
    private int memoryMB;

    @Column(name = "cpu_cores_number", nullable = false)
    private int cpuCoresNumber;

    @Column(name = "cpu_frequency_ghz", nullable = false)
    private float cpuFrequencyGHz;

    @Column(name = "installed_OS", nullable = false)
    private String installedOS;

    public NodeTechnicalProperties(int memoryMB, int cpuCoresNumber, float cpuFrequencyGHz, String installedOS) {
        this.memoryMB = memoryMB;
        this.cpuCoresNumber = cpuCoresNumber;
        this.cpuFrequencyGHz = cpuFrequencyGHz;
        this.installedOS = installedOS;
    }

    protected NodeTechnicalProperties() {
    }

    public int getMemoryMB() {
        return memoryMB;
    }

    public int getCpuCoresNumber() {
        return cpuCoresNumber;
    }

    public float getCpuFrequencyGHz() {
        return cpuFrequencyGHz;
    }

    public String getInstalledOS() {
        return installedOS;
    }
}
