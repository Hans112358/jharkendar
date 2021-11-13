import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import React from 'react';
import Fab from '@mui/material/Fab';
import CreateTagForm from './CreateTagForm';
import AddIcon from '@mui/icons-material/Add';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};


export default function CreateTagButton(props) {
    const [openCreateTagPopup, setOpenCreateTagPopup] = React.useState(false);
    const handleOpenCreateTagPopup = () => setOpenCreateTagPopup(true);
    const handleCloseCreateTagPopup = () => setOpenCreateTagPopup(false);

    return (
        <div>
            <Fab size="medium" color="primary" aria-label="add" onClick={handleOpenCreateTagPopup}>
                <AddIcon />
            </Fab>
            <Modal
                open={openCreateTagPopup}
                onClose={handleCloseCreateTagPopup}
            >
                <Box sx={style}>
                    <CreateTagForm handleCloseCreateTagPopup={handleCloseCreateTagPopup} handleOpenSuccessMessage={props.handleOpenSuccessMessage} />
                </Box>
            </Modal>
        </div>
    );
}