metadata {
        definition (name: "Virtual Switch 2", namespace: "paulmullen", author: "Paul Mullen") {
        capability "Switch"
        
        command remoteOn
        command remoteOff
    }

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("Switch", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "on", label: 'On', action: "switch.off", icon: "st.Lighting.light13", backgroundColor: "#00AA00"
			state "off", label: 'Off', action: "switch.on", icon: "st.Lighting.light13", backgroundColor: "#AAAAAA"
		}
		standardTile("OnButton", "device.button", width: 1, height: 1, canChangeIcon: false) {
			state "pushed", label: 'On', action: "switch.on", icon: "st.Kids.kid10"
		}
		standardTile("OffButton", "device.button", width: 1, height: 1, canChangeIcon: false) {
			state "pushed", label: 'Off', action: "switch.off", icon: "st.Kids.kid10"
		}
		main "Switch"
		details(["Switch","OnButton","OffButton"])
        
    }
}

def parse(String description) {
}

def on() {
	sendEvent(name: "switch", value: "on", isStateChange: "true")
}

def off() {
	sendEvent(name: "switch", value: "off", isStateChange: "true")
}

def remoteOn() {
	sendEvent(name: "switch", value: "on", isStateChange: "true")
}

def remoteOff() {
	sendEvent(name: "switch", value: "off", isStateChange: "true")
}
