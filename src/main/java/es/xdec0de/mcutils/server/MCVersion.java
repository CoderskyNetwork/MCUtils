package es.xdec0de.mcutils.server;

public enum MCVersion {

	V1_0,
	V1_1,
	V1_2,
	V1_3,
	V1_4,
	V1_5,
	V1_6,
	V1_7,
	V1_8,
	V1_9,
	V1_10,
	V1_11,
	V1_12,
	V1_13,
	V1_14,
	V1_15,
	V1_16,
	V1_17,
	V1_18,
	V1_19,
	UNKNOWN;

	public String getFormatName() {
		return this.equals(UNKNOWN) ? "Unknown" : name().replaceFirst("V", "").replace('_', '.');
	}

	public boolean supports(MCVersion version) {
		return ordinal() >= version.ordinal();
	}
}
