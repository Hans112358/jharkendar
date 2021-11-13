import React from 'react';
import {Grid} from "@mui/material";
import TextField from "@mui/material/TextField";
import SaveIcon from '@mui/icons-material/Save';
import Fab from "@mui/material/Fab";

export default function CreateTagForm(props) {
    const [tagName, handleNameChange] = React.useState("");
    const handleSubmit = (event) => {
        props.handleCloseCreateTagPopup();
        props.handleOpenSuccessMessage('Tag \'' + tagName + '\' created');
        event.preventDefault();
    }

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            handleSubmit(e);
        }
    }

    return (
        <div>
            <Grid container spacing={2}>
                <Grid item xs={9}>
                    <TextField
                        onChange={e => handleNameChange(e.target.value)}
                        value={tagName}
                        label={"Tag name"}
                        onKeyDown={e => handleKeyDown(e)}
                    />
                </Grid>
                <Grid item xs={3}>
                    <Fab size="medium" color="primary" aria-label="add" onClick={handleSubmit}>
                        <SaveIcon/>
                    </Fab>
                </Grid>
            </Grid>
        </div>
    );
}