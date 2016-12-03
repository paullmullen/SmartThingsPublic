 
 
metadata {
	definition (name: "PaulSmartInterface", author: "Mullen") {
    		capability "Switch Level"
            capability "Switch"
            capability "Sensor"
        	attribute "greeting","string"
            attribute "selected","string"
            attribute "message", "string"
            

            command "remoteOn"
            command "remoteOff"
	}

// Icons for smartthings can be found here:  http://scripts.3dgo.net/smartthings/icons/




	tiles {
		standardTile("switch", "device.switch", width: 1, height: 1, canChangeIcon: true, canChangeBackground: true) {
			state "on", label: 'Send', action: "switch.on", icon: "st.Lighting.light13", backgroundColor: "#ffffff"
			state "off", label: 'Send', action: "switch.on", icon: "st.Lighting.light13", backgroundColor: "#ffffff"
		}
		valueTile("selectedDevice", "device.greeting", inactiveLabel: false, width: 2, height: 1) {
			state "message", label:'${currentValue}'
        }
        controlTile("deviceSelector", "device.level", "slider", height: 1, width: 3, range: "(1..16)") {
            state "deviceSelector", action:"switch level.setLevel"
        }
        controlTile("houseSelector", "device.level", "slider", height: 1, width: 3, range: "(17..32)") {
            state "houseSelector", action:"switch level.setLevel"
        }
        valueTile("greeting", "device.greeting", inactiveLabel: false, width: 3, height: 1) {
			state "greeting", label:'${currentValue}'
        }
      
		main "switch"
		details(["switch","selectedDevice","houseSelector","deviceSelector","greeting"])
	}

}



Map parse(String description) {

	def value = zigbee.parse(description)?.text
	def linkText = getLinkText(device)
	def descriptionText = getDescriptionText(description, linkText, value)
	def handlerName = value
	def isStateChange = value != "ping"
	def displayed = value && isStateChange
	def result = [
		value: value,
		name: value in ["on","off"] ? "switch" : (value && value != "ping" ? "greeting" : null),
		handlerName: handlerName,
		linkText: linkText,
		descriptionText: descriptionText,
		isStateChange: isStateChange,
		displayed: displayed
	]
	result 
}




def on() {
   String houseCodes = "ABCDEFGHIJKLMNOP"
   log.debug "got ${houseCodes[state.houseNumber-1]}${state.deviceNumber} ON command"
	zigbee.smartShield(text: "${houseCodes[state.houseNumber-1]}${state.deviceNumber} on").format()

}

def off() {
   String houseCodes = "ABCDEFGHIJKLMNOP"
    log.debug "got ${houseCodes[state.houseNumber-1]}${state.deviceNumber} OFF command"
	zigbee.smartShield(text: "${houseCodes[state.houseNumber-1]}${state.deviceNumber} off").format()

}


def remoteOn(String relay) {
   zigbee.smartShield(text: "${relay} on").format()
   }

 //the onOff flag is sent from the SmartApp to indicate whether to send an X10 ON or and X10 OFF in response to an off command.   0=send Off, 1=send On
def remoteOff(String relay, Integer onOff) {                            
   if (onOff == 0) {
   zigbee.smartShield(text: "${relay} off").format()
      } else {
   zigbee.smartShield(text: "${relay} on").format()
      }
   }


def setLevel(rcdValue) {
   String houseCodes = "ABCDEFGHIJKLMNOP"
   if (rcdValue > 16) {
      state.houseNumber = rcdValue-16
   } else {
      state.deviceNumber = rcdValue
   }
   def result = sendEvent (name: "greeting", value: "${houseCodes[state.houseNumber-1]}${state.deviceNumber}");


 
}




def parse(String description) {

    def x10Message = zigbee.parse(description)?.text
    log.debug "value: " + x10Message

    

    switch (x10Message) {
    
       case "on":
          log.debug "Got an ON message."
          def result = createEvent(name: "switch", value: "on")
          break;
          
       case "off":
          log.debug "Got an OFF message."
          def result = createEvent(name: "switch", value: "off")
          break;

       default:
          def result = createEvent(name: "greeting", value: x10Message )
          log.trace result

          
          }

}
