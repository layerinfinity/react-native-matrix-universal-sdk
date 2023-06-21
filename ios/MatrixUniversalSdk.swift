import Foundation
import MatrixSDK

@objc(RNMatrixSDK)
class RNMatrixSDK: RCTEventEmitter {
    var E_MATRIX_ERROR: String! = "E_MATRIX_ERROR";
    var E_NETWORK_ERROR: String! = "E_NETWORK_ERROR";
    var E_UNEXPECTED_ERROR: String! = "E_UNKNOWN_ERROR";
    
    var mxSession: MXSession!
    var mxCredentials: MXCredentials!
    var mxHomeServer: URL!
    
    var roomEventsListeners: [String: Any] = [:]
    var roomPaginationTokens: [String : String] = [:]
    var globalListener: Any?
    var additionalTypes: [String] = []
    
    
    @objc
    override func supportedEvents() -> [String]! {
        var supportedTypes = ["matrix.room.backwards",
                              "matrix.room.forwards",
                              "m.fully_read",
                              MXEventType.roomName.identifier,
                              MXEventType.roomTopic.identifier,
                              MXEventType.roomAvatar.identifier,
                              MXEventType.roomMember.identifier,
                              MXEventType.roomCreate.identifier,
                              MXEventType.roomJoinRules.identifier,
                              MXEventType.roomPowerLevels.identifier,
                              MXEventType.roomAliases.identifier,
                              MXEventType.roomCanonicalAlias.identifier,
                              MXEventType.roomEncrypted.identifier,
                              MXEventType.roomEncryption.identifier,
                              MXEventType.roomGuestAccess.identifier,
                              MXEventType.roomHistoryVisibility.identifier,
                              MXEventType.roomKey.identifier,
                              MXEventType.roomForwardedKey.identifier,
                              MXEventType.roomKeyRequest.identifier,
                              MXEventType.roomMessage.identifier,
                              MXEventType.roomMessageFeedback.identifier,
                              MXEventType.roomRedaction.identifier,
                              MXEventType.roomThirdPartyInvite.identifier,
                              MXEventType.roomTag.identifier,
                              MXEventType.presence.identifier,
                              MXEventType.typing.identifier,
                              MXEventType.callInvite.identifier,
                              MXEventType.callCandidates.identifier,
                              MXEventType.callAnswer.identifier,
                              MXEventType.callHangup.identifier,
                              MXEventType.reaction.identifier,
                              MXEventType.receipt.identifier,
                              MXEventType.roomTombStone.identifier,
                              MXEventType.keyVerificationStart.identifier,
                              MXEventType.keyVerificationAccept.identifier,
                              MXEventType.keyVerificationKey.identifier,
                              MXEventType.keyVerificationMac.identifier,
                              MXEventType.keyVerificationCancel.identifier]
        // add any additional types the user provided
        supportedTypes += additionalTypes
        return supportedTypes;
    }
    
    @objc(setAdditionalEventTypes:)
    func setAdditionalEventTypes(types: [String]) {
        additionalTypes = types
    }
    
    
    @objc(createClient:resolver:rejecter:)
    func createClient(url: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        self.mxHomeServer = URL(string: url)
        
        if self.mxCredentials != nil {
            resolve(true)
        } else {
            reject(self.E_MATRIX_ERROR, nil, CustomError.alreadyInitialized)
        }
    }
    
    @objc(login:password:resolver:rejecter:)
    func login(username: String, password: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        if self.mxCredentials != nil {
            // Maybe change?
            resolve(true)
            return
        }
        
        // New user login
        let dummyCredentials = MXCredentials(homeServer: self.mxHomeServer.absoluteString, userId: nil, accessToken: nil)
        
        let restClient = MXRestClient.init(credentials: dummyCredentials, unrecognizedCertificateHandler: nil)
        let session = MXSession(matrixRestClient: restClient)
        
        session?.matrixRestClient.login(username: username, password: password, completion: { (credentials) in
            if credentials.isSuccess {
                self.mxCredentials = credentials.value
                // Maybe change?
                resolve(true)
                
                // Login completed => start the session
                // Code....
                
                if self.mxSession != nil && (self.mxSession.state == MXSessionState.initialised || self.mxSession.state == MXSessionState.running) {
                    // TODO: refactor to getMyUser and reuse
                    let user = self.mxSession.myUser
                    resolve([
                        "user_id": unNil(value: user?.userId),
                        "display_name": unNil(value: user?.displayname),
                        "avatar": unNil(value: user?.avatarUrl),
                        "last_active": unNil(value: user?.lastActiveAgo),
                        "status": unNil(value: user?.statusMsg),
                    ])
                    return
                }
                
            } else {
                reject(self.E_MATRIX_ERROR, nil, credentials.error)
            }
        })
        
        
    }
    
    @objc(setCredentials:deviceId:userId:homeServer:refreshToken:)
    func setCredentials(accessToken: String, deviceId: String, userId: String, homeServer: String, refreshToken: String) {
        let mxCredentials = MXCredentials()
        mxCredentials.accessToken = accessToken
        mxCredentials.deviceId = deviceId
        mxCredentials.userId = userId
        mxCredentials.homeServer = homeServer
        self.mxCredentials = mxCredentials;
    }
}

internal func unNil(value: Any?) -> Any? {
    guard let value = value else {
        return nil
    }
    return value
}

enum CustomError: Error {
    case alreadyInitialized
    case notFound
    case unexpected(code: Int)
}
