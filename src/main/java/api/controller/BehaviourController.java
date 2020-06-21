package api.controller;

import api.ResponseDTO;

public interface BehaviourController {

    ResponseEntity updateBehaviour(ResponseDTO responseDTO);

    ResponseEntity createBehaviour(ResponseDTO responseDTO);

    ResponseEntity deleteBehaviour(ResponseDTO responseDTO);

}
