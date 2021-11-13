import './App.css';
import CreateTagButton from './tag/CreateTagButton';
import React from "react";
import CssBaseline from '@mui/material/CssBaseline';
import Alert from "@mui/material/Alert";
import Snackbar from "@mui/material/Snackbar";


function App() {

    const [openSuccessMessage, setOpenSuccessMessage] = React.useState(false);
    const [message, setMessage] = React.useState(false);
    const handleOpenSuccessMessage = (successMessage) => {
        setMessage(successMessage);
        setOpenSuccessMessage(true);
    }
    const handleCloseSuccessMessage = () => setOpenSuccessMessage(false);

    return (
        <React.Fragment>
            <CssBaseline/>
            <CreateTagButton handleOpenSuccessMessage={handleOpenSuccessMessage}/>

            <Snackbar open={openSuccessMessage} autoHideDuration={6000} onClose={handleCloseSuccessMessage}>
                <Alert onClose={handleCloseSuccessMessage} severity="success" sx={{width: '100%'}}>
                    {message}
                </Alert>
            </Snackbar>

        </React.Fragment>
    );
}

export default App;
